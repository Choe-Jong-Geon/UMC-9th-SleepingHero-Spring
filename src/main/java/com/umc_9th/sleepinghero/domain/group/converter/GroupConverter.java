package com.umc_9th.sleepinghero.domain.group.converter;

import com.umc_9th.sleepinghero.domain.group.dto.req.GroupMakeRequestDto;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupRankResponse;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.group.enums.GroupRole;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.global.enums.Status;

public class GroupConverter {

    public static Group toGroup(GroupMakeRequestDto request, String masterNickName) {
        return Group.builder()
                .name(request.getGroupName())
                .description(request.getDescription())
                .groupImage(request.getGroupImageId())
                .master(masterNickName)
                .maxPeople(request.getMaxPeople() != null ? request.getMaxPeople() : 10)
                .currentPeople(0)
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

    public static GroupMember toGroupMember(Member member, Group group, GroupRole role, Status status){
        return GroupMember.builder()
                .member(member)
                .heroGroups(group)
                .groupRole(role)
                .status(status)
                .build();
    }

}
