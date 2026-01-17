package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import java.time.LocalDateTime;

public record SleepRecordResponse (
        Long sleepRecordId,
        LocalDateTime sleptTime,
        LocalDateTime wokeTime,
        boolean success
){
}
