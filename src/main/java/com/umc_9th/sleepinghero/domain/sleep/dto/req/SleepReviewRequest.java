package com.umc_9th.sleepinghero.domain.sleep.dto.req;

public record SleepReviewRequest(
        int star,
        String comment
) {
}
