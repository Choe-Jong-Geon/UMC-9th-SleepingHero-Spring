package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBackResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;

public interface SleepFeedBackService {


    AiSleepFeedBackResponse feedback(
            long sleepDuration, long goalDuration, int sleepStreak, SleepReviewRequest request
    );
}
