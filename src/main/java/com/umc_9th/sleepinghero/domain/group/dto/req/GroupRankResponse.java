package com.umc_9th.sleepinghero.domain.group.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRankResponse {

    private Long groupId;
    private String name;
    private int maxPeople;
    private int currentPeople;
    private int rank;

}
