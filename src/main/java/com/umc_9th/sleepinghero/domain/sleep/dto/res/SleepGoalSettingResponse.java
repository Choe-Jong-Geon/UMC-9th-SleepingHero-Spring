package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import java.time.LocalTime;

public record SleepGoalSettingResponse(
        LocalTime sleepTime,
        LocalTime wakeTime,
        long totalMinutes
) {
}
