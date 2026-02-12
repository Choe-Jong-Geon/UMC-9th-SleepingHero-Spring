package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import java.time.LocalDateTime;

public record SleepRecordResponse (
        Long recordId,
        int star,
        LocalDateTime sleptTime,
        LocalDateTime wokeTime,
        long totalMinutes,
        String summary
){
}
