package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import java.time.LocalDate;
import java.util.List;

public record SleepRecordResponse (
        Long recordId,
        int star,
        LocalDate date,
        long totalMinutes,
        String summary,
        List<String> positives,
        List<String> improvements,
        String cheering
){
}
