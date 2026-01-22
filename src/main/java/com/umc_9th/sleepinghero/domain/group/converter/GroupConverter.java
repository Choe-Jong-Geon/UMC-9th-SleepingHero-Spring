package com.umc_9th.sleepinghero.domain.group.converter;

import com.umc_9th.sleepinghero.domain.group.dto.req.GroupMakeRequestDto;
import com.umc_9th.sleepinghero.domain.group.dto.req.GroupRankResponse;
import com.umc_9th.sleepinghero.domain.group.entity.Group;

public class GroupConverter {

    public static Group toGroup(GroupMakeRequestDto request, String masterNickName) {
        return Group.builder()
                .name(request.getGroupName())
                .description(request.getDescription())
                .master(masterNickName)
                .maxPeople(request.getMaxPeople() != null ? request.getMaxPeople() : 10)
                .currentPeople(1) // 생성자 본인이 첫 번째 멤버이므로 1로 시작
                .build();
    }

    public static GroupRankResponse toGroupRankResponse(Group group, int rank) {
        return GroupRankResponse.builder()
                .groupId(group.getId())
                .name(group.getName())
                .maxPeople(group.getMaxPeople())
                .currentPeople(group.getCurrentPeople())
                .rank(rank)
                .build();
    }

}
