package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import java.time.LocalDateTime;

public record SleepRecordResponse (
        Long recordId,
        LocalDateTime sleptTime,
        LocalDateTime wokeTime,
        boolean isSuccess
){
}
