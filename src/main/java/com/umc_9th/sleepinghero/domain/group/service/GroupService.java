package com.umc_9th.sleepinghero.domain.group.service;

import com.umc_9th.sleepinghero.domain.group.calculator.GroupStatisticsCalculator;
import com.umc_9th.sleepinghero.domain.group.converter.GroupConverter;
import com.umc_9th.sleepinghero.domain.group.dto.req.*;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInsideRankingResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInvitationResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupRankResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.MemberRankingInfo;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.group.enums.GroupRole;
import com.umc_9th.sleepinghero.domain.group.exception.GroupErrorCode;
import com.umc_9th.sleepinghero.domain.group.repository.GroupMemberRepository;
import com.umc_9th.sleepinghero.domain.group.repository.GroupRepository;
import com.umc_9th.sleepinghero.domain.group.validator.GroupValidator;
import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;

import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.global.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MemberRepository memberRepository;
    private final HeroRepository heroRepository;

    private final GroupValidator groupValidator;
    private final GroupStatisticsCalculator groupCalculator;

    public String createGroup(Long memberId, GroupMakeRequestDto request) {
        groupValidator.validateGroupRequest(request);
        Member member = findMemberByIdOrThrow(memberId);

        Group group = groupRepository.save(GroupConverter.toGroup(request, member.getNickName()));
        group.incrementCurrentPeople();

        groupMemberRepository.save(GroupConverter.toGroupMember(member, group, GroupRole.LEADER, Status.APPROVE));

        return "그룹 생성이 완료되었습니다.";
    }

    @Transactional(readOnly = true)
    public List<GroupRankResponse> getGroupRanking() {
        List<Group> groups = groupRepository.findAll();
        Map<Long, Double> groupSleepAvgMap = groups.stream()
                .collect(Collectors.toMap(Group::getId, g -> groupCalculator.calculateGroupSleepAverage(g.getId())));

        return IntStream.range(0, groups.size())
                .boxed()
                .sorted((i, j) -> getGroupComparator(groupSleepAvgMap).compare(groups.get(i), groups.get(j)))
                .map(i -> GroupConverter.toGroupRankResponse(groups.get(i), i + 1))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupInsideRankingResponse getGroupInsideRanking(String groupName) {
        Group group = findGroupByNameOrThrow(groupName);
        List<GroupMember> acceptedMembers = groupMemberRepository.findAllByHeroGroupsAndStatus(group, Status.APPROVE);

        List<MemberRankingInfo> rankings = acceptedMembers.stream()
                .map(this::mapToMemberRankingInfo)
                .sorted(Comparator.comparing(MemberRankingInfo::getTotalSleepTime).reversed())
                .collect(Collectors.toList());

        IntStream.range(0, rankings.size()).forEach(i -> {
            MemberRankingInfo info = rankings.get(i);
            rankings.set(i, MemberRankingInfo.builder()
                    .rank(i + 1).memberName(info.getMemberName()).groupRole(info.getGroupRole())
                    .consecutiveSleepDays(info.getConsecutiveSleepDays()).totalSleepTime(info.getTotalSleepTime())
                    .level(info.getLevel()).build());
        });

        long totalTime = rankings.stream().mapToLong(MemberRankingInfo::getTotalSleepTime).sum();
        double avgDays = rankings.stream().mapToInt(MemberRankingInfo::getConsecutiveSleepDays).average().orElse(0.0);

        return GroupConverter.toGroupInsideRankingResponse(group, rankings, totalTime, avgDays);
    }

    public String inviteMember(Long memberId, GroupInvitationRequest request) {
        Member me = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());
        groupValidator.validateMasterAuthority(group, me.getNickName());

        Member invitee = findMemberByNickNameOrThrow(request.getNickName());
        groupValidator.validateInvitationEligibility(invitee, group);

        groupMemberRepository.save(GroupConverter.toGroupMember(invitee, group, GroupRole.USER, Status.PENDING));
        return "그룹 초대가 완료되었습니다.";
    }

    @Transactional(readOnly = true)
    public List<GroupInvitationResponse> getPendingGroupRequests(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);

        return groupMemberRepository.findAllByMemberAndStatus(member, Status.PENDING).stream()
                .map(gm -> GroupInvitationResponse.builder()
                        .groupName(gm.getHeroGroups().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public String processGroupInvitation(Long memberId, String groupName, String status) {
        Member me = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(groupName);
        GroupMember invitation = findPendingInvitationOrThrow(me, group);

        if ("APPROVE".equals(status)) {
            groupValidator.validateGroupCapacity(group);
            invitation.updateStatus(Status.APPROVE);
            group.incrementCurrentPeople();
            return "그룹 가입 요청을 수락하였습니다.";
        } else if ("REJECTED".equals(status)) {
            groupMemberRepository.delete(invitation);
            return "그룹 가입 요청을 거부하였습니다.";
        }
        return "APPROVE, REJECTED 중 하나를 정확하게 입력하여 주세요";
    }

    public String exitOrKickGroup(Long memberId, GroupExitRequest request) {
        Member actor = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());

        if (isKickScenario(request)) {
            groupValidator.validateMasterAuthority(group, actor.getNickName());
            groupValidator.validateNotSelfKick(actor.getNickName(), request.getNickName());
            return removeMember(findMemberByNickNameOrThrow(request.getNickName()), group, "추방");
        }

        groupValidator.validateNotMasterLeave(group, actor.getNickName());
        return removeMember(actor, group, "탈퇴");
    }

    public String deleteGroup(Long memberId, GroupDeleteRequest request) {
        Member actor = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());

        groupValidator.validateMasterAuthority(group, actor.getNickName());
        groupValidator.validateDeletableCondition(group);

        groupMemberRepository.deleteAll(groupMemberRepository.findAllByHeroGroupsIdAndStatus(group.getId(), Status.APPROVE));
        groupRepository.delete(group);
        return "그룹이 삭제되었습니다.";
    }

    private MemberRankingInfo mapToMemberRankingInfo(GroupMember gm) {
        Member m = gm.getMember();
        int level = heroRepository.findByMember(m).map(Hero::getCurrentLevel).orElse(1);
        long totalHours = groupCalculator.calculateTotalSleepSeconds(m) / 3600;
        int consecutiveDays = groupCalculator.calculateConsecutiveDays(m);
        return GroupConverter.toMemberRankingInfo(gm, level, totalHours, consecutiveDays);
    }

    private String removeMember(Member target, Group group, String type) {
        GroupMember gm = findAcceptedMemberOrThrow(target, group);
        groupMemberRepository.delete(gm);
        group.decrementCurrentPeople();
        return type.equals("추방") ? String.format("'%s'를 추방하였습니다.", target.getNickName()) : "그룹을 탈퇴하였습니다.";
    }

    private Member findMemberByIdOrThrow(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findMemberByNickNameOrThrow(String name) {
        return memberRepository.findByNickName(name).orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Group findGroupByNameOrThrow(String name) {
        return groupRepository.findByName(name).orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private GroupMember findAcceptedMemberOrThrow(Member m, Group g) {
        return groupMemberRepository.findByMemberAndHeroGroupsAndStatus(m, g, Status.APPROVE)
                .orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private GroupMember findPendingInvitationOrThrow(Member m, Group g) {
        return groupMemberRepository.findByMemberAndHeroGroupsAndStatus(m, g, Status.PENDING)
                .orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private boolean isKickScenario(GroupExitRequest r) {
        return r.getNickName() != null && !r.getNickName().isBlank();
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
}