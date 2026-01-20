package com.umc_9th.sleepinghero.domain.group.service;

import com.umc_9th.sleepinghero.domain.group.converter.GroupConverter;
import com.umc_9th.sleepinghero.domain.group.dto.req.*;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.group.exception.GroupErrorCode;
import com.umc_9th.sleepinghero.domain.group.repository.GroupMemberRepository;
import com.umc_9th.sleepinghero.domain.group.repository.GroupRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.umc_9th.sleepinghero.domain.group.enums.GroupRole.USER;


@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SleepRecordRepository sleepRecordRepository;
    private final MemberRepository memberRepository;

    public String createGroup(Long memberId, GroupMakeRequestDto request) {
        validateGroupRequest(request);
        Member member = findMemberByIdOrThrow(memberId);
        groupRepository.save(GroupConverter.toGroup(request, member.getNickName()));
        return "그룹 생성이 완료되었습니다.";
    }

    @Transactional(readOnly = true)
    public List<GroupRankResponse> getGroupRanking() {
        List<Group> groups = groupRepository.findAll();
        Map<Long, Double> groupSleepAvgMap = calculateAllGroupsSleepAverage(groups);

        List<Group> sortedGroups = groups.stream()
                .sorted(getGroupComparator(groupSleepAvgMap))
                .collect(Collectors.toList());

        return IntStream.range(0, sortedGroups.size())
                .mapToObj(i -> GroupConverter.toGroupRankResponse(sortedGroups.get(i), i + 1))
                .collect(Collectors.toList());
    }

    public String inviteMember(Long memberId, GroupInvitationRequest request) {
        Member me = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());

        validateMasterAuthority(group, me.getNickName());

        Member invitee = findMemberByNickNameOrThrow(request.getNickName());
        validateInvitationEligibility(invitee, group);

        groupMemberRepository.save(GroupConverter.toGroupMember(invitee, group, USER, Status.PENDING));
        return "그룹 초대/가입 요청이 완료되었습니다.";
    }

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

    public String exitOrKickGroup(Long memberId, GroupExitRequest request) {
        Member loginMember = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());

        if (isKickScenario(request)) {
            return kickMember(loginMember, group, request.getNickName());
        }
        return leaveGroup(loginMember, group);
    }

    public String deleteGroup(Long memberId, GroupDeleteRequest request) {
        Member loginMember = findMemberByIdOrThrow(memberId);
        Group group = findGroupByNameOrThrow(request.getGroupName());

        validateMasterAuthority(group, loginMember.getNickName());
        validateDeletableCondition(group);

        List<GroupMember> masterRelation = groupMemberRepository.findAllByHeroGroupsIdAndStatus(group.getId(), Status.APPROVE);
        groupMemberRepository.deleteAll(masterRelation);
        groupRepository.delete(group);

        return "그룹이 삭제되었습니다.";
    }

    //------------------------------------------private logic ------------------------------------

    private String kickMember(Member admin, Group group, String targetNickName) {
        validateMasterAuthority(group, admin.getNickName());
        validateNotSelfKick(admin.getNickName(), targetNickName);

        Member targetMember = findMemberByNickNameOrThrow(targetNickName);
        GroupMember groupMember = findAcceptedMemberOrThrow(targetMember, group);

        groupMemberRepository.delete(groupMember);
        group.decrementCurrentPeople();
        return "'" + targetNickName + "'를 추방하였습니다.";
    }

    private String leaveGroup(Member member, Group group) {
        validateNotMasterLeave(group, member.getNickName());
        GroupMember groupMember = findAcceptedMemberOrThrow(member, group);

        groupMemberRepository.delete(groupMember);
        group.decrementCurrentPeople();
        return "그룹을 탈퇴하였습니다.";
    }

    private Member findMemberByIdOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findMemberByNickNameOrThrow(String nickName) {
        return memberRepository.findByNickName(nickName)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    // (기타 validate 및 find 메서드는 기존과 동일하되 인자값만 조절)
    private Group findGroupByNameOrThrow(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private GroupMember findPendingInvitationOrThrow(Member member, Group group) {
        return groupMemberRepository.findByMemberAndHeroGroupsAndStatus(member, group, Status.PENDING)
                .orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private GroupMember findAcceptedMemberOrThrow(Member member, Group group) {
        return groupMemberRepository.findByMemberAndHeroGroupsAndStatus(member, group, Status.APPROVE)
                .orElseThrow(() -> new GeneralException(GroupErrorCode.GROUP_NOT_FOUND));
    }

    private void validateGroupRequest(GroupMakeRequestDto request) {
        if (request.getGroupName() == null || request.getDescription() == null) {
            throw new GeneralException(GroupErrorCode.GROUP_NOT_MADE);
        }
    }

    private void validateMasterAuthority(Group group, String nickName) {
        if (!group.getMaster().equals(nickName)) {
            throw new GeneralException(GroupErrorCode.NOT_GROUP_MASTER);
        }
    }

    private void validateDeletableCondition(Group group) {
        if (group.getCurrentPeople() > 1) {
            throw new GeneralException(GroupErrorCode.GROUP_NOT_DELETED);
        }
    }

    private void validateInvitationEligibility(Member invitee, Group group) {
        if (groupMemberRepository.existsByMemberAndHeroGroups(invitee, group)) {
            throw new GeneralException(MemberErrorCode.FRIEND_ALREADY_EXISTS);
        }
        if (group.getCurrentPeople() >= group.getMaxPeople()) {
            throw new GeneralException(GroupErrorCode.GROUP_FULL);
        }
    }

    private void validateGroupCapacity(Group group) {
        if (group.getCurrentPeople() >= group.getMaxPeople()) {
            throw new GeneralException(GroupErrorCode.GROUP_FULL);
        }
    }

    private void validateNotSelfKick(String adminNick, String targetNick) {
        if (adminNick.equals(targetNick)) {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }
    }

    private void validateNotMasterLeave(Group group, String nickName) {
        if (group.getMaster().equals(nickName)) {
            throw new GeneralException(GroupErrorCode.MASTER_NOT_EXITED);
        }
    }

    private boolean isKickScenario(GroupExitRequest request) {
        return request.getNickName() != null && !request.getNickName().isBlank();
    }

    private Comparator<Group> getGroupComparator(Map<Long, Double> groupSleepAvgMap) {
        return (g1, g2) -> {
            if (g1.getCurrentPeople() != g2.getCurrentPeople()) {
                return Integer.compare(g2.getCurrentPeople(), g1.getCurrentPeople());
            }
            return Double.compare(groupSleepAvgMap.get(g2.getId()), groupSleepAvgMap.get(g1.getId()));
        };
    }

    private Map<Long, Double> calculateAllGroupsSleepAverage(List<Group> groups) {
        return groups.stream().collect(Collectors.toMap(Group::getId, group -> calculateGroupSleepAverage(group.getId())));
    }

    private double calculateGroupSleepAverage(Long groupId) {
        List<GroupMember> groupMembers = groupMemberRepository.findAllByHeroGroupsIdAndStatus(groupId, Status.APPROVE);
        if (groupMembers.isEmpty()) return 0.0;

        return groupMembers.stream()
                .map(GroupMember::getMember)
                .flatMap(member -> sleepRecordRepository.findAllByMemberAndSuccess(member, true).stream())
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).toMinutes())
                .average()
                .orElse(0.0);
    }

}
