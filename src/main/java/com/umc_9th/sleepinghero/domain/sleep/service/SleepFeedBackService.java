package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;

public interface SleepFeedBackService {


    Object feedback(
            long sleepDuration, long goalDuration, int sleepStreak, SleepReviewRequest request
    );
}
