package com.umc_9th.sleepinghero.domain.group.converter;

import com.umc_9th.sleepinghero.domain.group.dto.req.GroupMakeRequestDto;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInsideRankingResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupRankResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.MemberRankingInfo;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.group.enums.GroupRole;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.global.enums.Status;

import java.util.List;

public class GroupConverter {

    public static Group toGroup(GroupMakeRequestDto request, String masterNickname) {
        return Group.builder()
                .name(request.getGroupName())
                .description(request.getDescription())
                .master(masterNickname)
                .groupImage(request.getGroupImageId())
                .maxPeople(request.getMaxPeople() != null ? request.getMaxPeople() : 10)
                .currentPeople(0)
                .build();
    }

    public static GroupMember toGroupMember(Member member, Group group, GroupRole role, Status status) {
        return GroupMember.builder()
                .member(member)
                .heroGroups(group)
                .groupRole(role)
                .status(status)
                .build();
    }

    public static MemberRankingInfo toMemberRankingInfo(GroupMember gm, int level, long totalHours, int consecutiveDays) {
        return MemberRankingInfo.builder()
                .memberName(gm.getMember().getNickName())
                .groupRole(gm.getGroupRole().toString())
                .totalSleepTime(totalHours)
                .consecutiveSleepDays(consecutiveDays)
                .level(level)
                .build();
    }

    public static GroupRankResponse toGroupRankResponse(Group group, int rank) {
        return GroupRankResponse.builder()
                .groupId(group.getId())
                .name(group.getName())
                .maxPeople(group.getMaxPeople())
                .currentPeople(group.getCurrentPeople())
                .groupImageId(group.getGroupImage())
                .rank(rank)
                .build();
    }

    public static GroupInsideRankingResponse toGroupInsideRankingResponse(Group group, List<MemberRankingInfo> rankings, long totalTime, double avgDays) {
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
}
