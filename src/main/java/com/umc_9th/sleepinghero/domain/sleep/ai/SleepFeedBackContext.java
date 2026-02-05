package com.umc_9th.sleepinghero.domain.sleep.ai;

import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;

public record SleepFeedBackContext(
        int sleepDuration,
        int goalDuration,
        int sleepStreak,
        SleepReviewRequest review
){
    public static SleepFeedBackContext of(
            int sleepDuration, int goalDuration
            , int sleepStreak, SleepReviewRequest review) {

        return new SleepFeedBackContext(
                sleepDuration, goalDuration, sleepStreak, review
        );
    }
}
