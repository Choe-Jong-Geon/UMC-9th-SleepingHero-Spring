package com.umc_9th.sleepinghero.domain.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendRankResponse {
    private String nickName;
    private String totalSleepTime; // "10시간 30분 15초"
    private int rank;
}
