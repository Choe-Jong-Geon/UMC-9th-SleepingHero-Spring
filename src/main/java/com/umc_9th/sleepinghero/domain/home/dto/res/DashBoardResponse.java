package com.umc_9th.sleepinghero.domain.home.dto.res;


public record DashBoardResponse(
        Long heroId,
        int currentStage,
        int currentStreak,
        int nonSleepStreak
) {
}
