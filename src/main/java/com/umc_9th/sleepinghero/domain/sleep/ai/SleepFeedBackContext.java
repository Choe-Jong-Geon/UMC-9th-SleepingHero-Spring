package com.umc_9th.sleepinghero.domain.sleep.ai;

import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;

public record SleepFeedBackContext(
        long sleepDuration,
        long goalDuration,
        int sleepStreak,
        SleepReviewRequest review
){
    public static SleepFeedBackContext of(
            long sleepDuration, long goalDuration
            , int sleepStreak, SleepReviewRequest review) {

        return new SleepFeedBackContext(
                sleepDuration, goalDuration, sleepStreak, review
        );
    }
}
