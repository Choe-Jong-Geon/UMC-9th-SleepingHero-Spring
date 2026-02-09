package com.umc_9th.sleepinghero.domain.group.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberRankingInfo {
    private int rank;
    private String memberName;
    private String groupRole;
    private int consecutiveSleepDays;
    private long totalSleepTime;
    private int level;
}
