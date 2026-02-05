package com.umc_9th.sleepinghero.domain.sleep.dto.req;

public record SleepReviewRequest(
        Long id,
        int star,
        String comment
) {
}
