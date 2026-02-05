package com.umc_9th.sleepinghero.domain.sleep.ai;

import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;

public record AiSleepFeedBackContext(
        long sleepDuration,
        long goalDuration,
        int sleepStreak,
        SleepReviewRequest review
){
    public static AiSleepFeedBackContext of(
            long sleepDuration, long goalDuration
            , int sleepStreak, SleepReviewRequest review) {

        return new AiSleepFeedBackContext(
                sleepDuration, goalDuration, sleepStreak, review
        );
    }
}
