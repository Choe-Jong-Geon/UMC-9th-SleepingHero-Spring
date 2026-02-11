package com.umc_9th.sleepinghero.domain.group.service;

import com.umc_9th.sleepinghero.domain.group.converter.GroupConverter;
import com.umc_9th.sleepinghero.domain.group.dto.req.*;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInsideRankingResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupRankResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.MemberRankingInfo;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.group.enums.GroupRole;
import com.umc_9th.sleepinghero.domain.group.exception.GroupErrorCode;
import com.umc_9th.sleepinghero.domain.group.repository.GroupMemberRepository;
import com.umc_9th.sleepinghero.domain.group.repository.GroupRepository;
import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;

import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.global.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SleepRecordRepository sleepRecordRepository;
    private final MemberRepository memberRepository;
    private final HeroRepository heroRepository;

    // 1. 그룹 생성
    public String createGroup(Long memberId, GroupMakeRequestDto request) {
        validateGroupRequest(request);
        Member member = findMemberByIdOrThrow(memberId);

        Group newGroup = GroupConverter.toGroup(request, member.getNickName());
        newGroup.incrementCurrentPeople(); // 인원수 +1
        Group savedGroup = groupRepository.save(newGroup);

        GroupMember groupMaster = GroupMember.builder()
                .member(member)
                .heroGroups(savedGroup)
                .groupRole(GroupRole.LEADER) // enum에 MASTER가 있다고 가정
                .status(Status.APPROVE)      // 생성자는 대기 없이 바로 승인 상태
                .build();

        groupMemberRepository.save(groupMaster);

        return "그룹 생성이 완료되었습니다.";
    }

    // 2. 전체 그룹 랭킹 조회
    @Transactional(readOnly = true)
    public List<GroupRankResponse> getGroupRanking() {
        List<Group> groups = groupRepository.findAll();
        Map<Long, Double> groupSleepAvgMap = calculateAllGroupsSleepAverage(groups);

        return IntStream.range(0, groups.size())
                .boxed()
                .sorted((i, j) -> getGroupComparator(groupSleepAvgMap).compare(groups.get(i), groups.get(j)))
                .map(i -> GroupConverter.toGroupRankResponse(groups.get(i), i + 1))
                .collect(Collectors.toList());
    }

    // 3. 그룹 내 랭킹 조회
    @Transactional(readOnly = true)
    public GroupInsideRankingResponse getGroupInsideRanking(String groupName) {
        Group group = findGroupByNameOrThrow(groupName);
        List<GroupMember> acceptedMembers = groupMemberRepository.findAllByHeroGroupsAndStatus(group, Status.APPROVE);

        try {
            List<MemberRankingInfo> rankings = acceptedMembers.stream()
                    .map(this::createMemberRankingInfo)
                    .sorted(Comparator.comparing(MemberRankingInfo::getTotalSleepTime).reversed())
                    .collect(Collectors.toList());

            for (int i = 0; i < rankings.size(); i++) {
                rankings.set(i, updateRank(rankings.get(i), i + 1));
            }

            return buildGroupInsideRankingResponse(group, rankings);
        } catch (Exception e) {
            throw new GeneralException(GroupErrorCode.GROUP_INSIDE_RANKING_ERROR);
        }
    }

    // 4. 멤버 초대
    public String inviteMember(Long memberId, GroupInvitationRequest request) {
        Member me = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());
        validateMasterAuthority(group, me.getNickName());

        Member invitee = findMemberByNickNameOrThrow(request.getNickName());
        validateInvitationEligibility(invitee, group);

        groupMemberRepository.save(GroupConverter.toGroupMember(invitee, group, GroupRole.USER, Status.PENDING));
        return "그룹 초대/가입 요청이 완료되었습니다.";
    }

    // 5. 초대 승인/거절
    public String processGroupInvitation(Long memberId, String groupName, String status) {
        Member me = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(groupName);
        GroupMember invitation = findPendingInvitationOrThrow(me, group);

        if ("accept".equalsIgnoreCase(status)) {
            validateGroupCapacity(group);
            invitation.updateStatus(Status.APPROVE);
            group.incrementCurrentPeople();
            return "그룹 가입 요청을 수락하였습니다.";
        }

        groupMemberRepository.delete(invitation);
        return "그룹 가입 요청을 거부하였습니다.";
    }

    // 6. 탈퇴 및 추방
    public String exitOrKickGroup(Long memberId, GroupExitRequest request) {
        Member loginMember = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());

        if (isKickScenario(request)) {
            validateMasterAuthority(group, loginMember.getNickName());
            validateNotSelfKick(loginMember.getNickName(), request.getNickName());
            return processMemberRemoval(findMemberByNickNameOrThrow(request.getNickName()), group, "추방");
        }

        validateNotMasterLeave(group, loginMember.getNickName());
        return processMemberRemoval(loginMember, group, "탈퇴");
    }

    // 7. 그룹 삭제
    public String deleteGroup(Long memberId, GroupDeleteRequest request) {
        Member loginMember = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());

        validateMasterAuthority(group, loginMember.getNickName());
        validateDeletableCondition(group);

        groupMemberRepository.deleteAll(groupMemberRepository.findAllByHeroGroupsIdAndStatus(group.getId(), Status.APPROVE));
        groupRepository.delete(group);
        return "그룹이 삭제되었습니다.";
    }

    // ----------------- Private Helpers: 조회 -----------------

    private Member findMemberByIdOrThrow(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findMemberByNickNameOrThrow(String nickName) {
        return memberRepository.findByNickName(nickName).orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Group findGroupByNameOrThrow(String name) {
        return groupRepository.findByName(name).orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private GroupMember findAcceptedMemberOrThrow(Member member, Group group) {
        return groupMemberRepository.findByMemberAndHeroGroupsAndStatus(member, group, Status.APPROVE)
                .orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private GroupMember findPendingInvitationOrThrow(Member member, Group group) {
        return groupMemberRepository.findByMemberAndHeroGroupsAndStatus(member, group, Status.PENDING)
                .orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    // ----------------- Private Helpers: 비즈니스 로직 -----------------

    private String processMemberRemoval(Member member, Group group, String actionType) {
        GroupMember gm = findAcceptedMemberOrThrow(member, group);
        groupMemberRepository.delete(gm);
        group.decrementCurrentPeople();
        return actionType.equals("추방") ? "'" + member.getNickName() + "'를 추방하였습니다." : "그룹을 탈퇴하였습니다.";
    }

    private MemberRankingInfo createMemberRankingInfo(GroupMember gm) {
        Member member = gm.getMember();
        int level = heroRepository.findByMember(member).map(Hero::getCurrentLevel).orElse(1);
        return MemberRankingInfo.builder()
                .memberName(member.getNickName())
                .groupRole(gm.getGroupRole().toString())
                .totalSleepTime(calculateTotalSleepSeconds(member) / 3600)
                .consecutiveSleepDays(calculateConsecutiveDays(member))
                .level(level)
                .build();
    }

    private GroupInsideRankingResponse buildGroupInsideRankingResponse(Group group, List<MemberRankingInfo> rankings) {
        long totalTime = rankings.stream().mapToLong(MemberRankingInfo::getTotalSleepTime).sum();
        double avgDays = rankings.stream().mapToInt(MemberRankingInfo::getConsecutiveSleepDays).average().orElse(0.0);
        return GroupInsideRankingResponse.builder()
                .groupName(group.getName())
                .description(group.getDescription())
                .totalMembers(group.getCurrentPeople())
                .totalGroupSleepTime(totalTime)
                .groupImageId(group.getGroupImage())
                .averageConsecutiveDays(Math.round(avgDays * 10) / 10.0)
                .memberRankings(rankings)
                .groupMasterNickname(group.getMaster())
                .build();
    }

    private Comparator<Group> getGroupComparator(Map<Long, Double> groupSleepAvgMap) {
        return (g1, g2) -> {
            if (g1.getCurrentPeople() != g2.getCurrentPeople()) {
                return Integer.compare(g2.getCurrentPeople(), g1.getCurrentPeople());
            }
            return Double.compare(groupSleepAvgMap.getOrDefault(g2.getId(), 0.0),
                    groupSleepAvgMap.getOrDefault(g1.getId(), 0.0));
        };
    }

    // ----------------- Private Helpers: 검증 -----------------

    private void validateMasterAuthority(Group group, String nickName) {
        if (!group.getMaster().equals(nickName)) throw new GeneralException(GroupErrorCode.NOT_GROUP_MASTER);
    }

    private void validateGroupCapacity(Group group) {
        if (group.getCurrentPeople() >= group.getMaxPeople()) throw new GeneralException(GroupErrorCode.GROUP_FULL);
    }

    // (기타 validate 메서드는 명칭과 구조를 유지하며 중복 로직 제거)
    private void validateGroupRequest(GroupMakeRequestDto r) { if (r.getGroupName() == null || r.getDescription() == null) throw new GeneralException(GroupErrorCode.GROUP_NOT_MADE); }
    private void validateDeletableCondition(Group g) { if (g.getCurrentPeople() > 1) throw new GeneralException(GroupErrorCode.GROUP_NOT_DELETED); }
    private void validateNotSelfKick(String a, String t) { if (a.equals(t)) throw new GeneralException(GeneralErrorCode.BAD_REQUEST); }
    private void validateNotMasterLeave(Group g, String n) { if (g.getMaster().equals(n)) throw new GeneralException(GroupErrorCode.MASTER_NOT_EXITED); }
    private void validateInvitationEligibility(Member i, Group g) { if (groupMemberRepository.existsByMemberAndHeroGroups(i, g)) throw new GeneralException(MemberErrorCode.FRIEND_ALREADY_EXISTS); validateGroupCapacity(g); }
    private boolean isKickScenario(GroupExitRequest r) { return r.getNickName() != null && !r.getNickName().isBlank(); }

    // ----------------- Private Helpers: 수면 연산 -----------------

    private long calculateTotalSleepSeconds(Member m) {
        return sleepRecordRepository.findAllByMemberAndIsSuccess(m, true).stream()
                .filter(sr -> sr.getSleptTime() != null && sr.getWokeTime() != null)
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).getSeconds()).sum();
    }

    private int calculateConsecutiveDays(Member m) {
        List<LocalDate> dates = sleepRecordRepository.findAllByMemberAndIsSuccess(m, true).stream()
                .map(sr -> sr.getSleptTime().toLocalDate()).distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        if (dates.isEmpty()) return 0;
        LocalDate target = dates.get(0);
        if (!target.equals(LocalDate.now()) && !target.equals(LocalDate.now().minusDays(1))) return 0;
        int count = 0;
        for (LocalDate d : dates) { if (d.equals(target)) { count++; target = target.minusDays(1); } else break; }
        return count;
    }

    private Map<Long, Double> calculateAllGroupsSleepAverage(List<Group> gs) {
        return gs.stream().collect(Collectors.toMap(Group::getId, g -> calculateGroupSleepAverage(g.getId())));
    }

    private double calculateGroupSleepAverage(Long gid) {
        List<GroupMember> gms = groupMemberRepository.findAllByHeroGroupsIdAndStatus(gid, Status.APPROVE);
        return gms.stream().map(GroupMember::getMember)
                .flatMap(m -> sleepRecordRepository.findAllByMemberAndIsSuccess(m, true).stream())
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).toMinutes())
                .average().orElse(0.0);
    }

    private MemberRankingInfo updateRank(MemberRankingInfo i, int r) {
        return MemberRankingInfo.builder().rank(r).memberName(i.getMemberName()).groupRole(i.getGroupRole()).consecutiveSleepDays(i.getConsecutiveSleepDays()).totalSleepTime(i.getTotalSleepTime()).level(i.getLevel()).build();
    }
}