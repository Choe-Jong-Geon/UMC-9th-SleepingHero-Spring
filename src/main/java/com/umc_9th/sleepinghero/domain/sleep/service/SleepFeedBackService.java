package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;


public interface SleepFeedBackService {


    AiSleepFeedBack feedback(
            long sleepDuration, long goalDuration, int sleepStreak, SleepReview review
    );
}
