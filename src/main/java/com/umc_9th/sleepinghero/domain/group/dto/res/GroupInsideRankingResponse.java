package com.umc_9th.sleepinghero.domain.group.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GroupInsideRankingResponse {
    private String groupName;
    private String description;
    private int totalMembers;
    private long totalGroupSleepTime;
    private double averageConsecutiveDays;
    private List<MemberRankingInfo> memberRankings;
}
