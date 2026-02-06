package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBackResponse;

public record SleepReviewResponse(
        AiSleepFeedBackResponse feedBack
) {
}
