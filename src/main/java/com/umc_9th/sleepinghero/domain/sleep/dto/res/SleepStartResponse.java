package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import java.time.LocalDateTime;

public record SleepStartResponse(
        Long sleepRecordId,
        LocalDateTime sleepTime,
        LocalDateTime wakeTime
){
}
