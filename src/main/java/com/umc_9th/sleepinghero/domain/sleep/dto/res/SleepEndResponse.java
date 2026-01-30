package com.umc_9th.sleepinghero.domain.sleep.dto.res;


import java.time.LocalDateTime;

public record SleepEndResponse(

        Long sleepRecordId,
        LocalDateTime sleptTime,
        LocalDateTime wokeTime,
        long durationMinutes,

        SleepReward sleepReward,

        int currentStage
) {
}

