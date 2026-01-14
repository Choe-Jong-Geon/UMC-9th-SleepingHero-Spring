package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import java.time.LocalTime;

public record SleepStartResponse(
        Long sleepRecordId,
        LocalTime sleepTime,
        LocalTime wakeTime
){
}
