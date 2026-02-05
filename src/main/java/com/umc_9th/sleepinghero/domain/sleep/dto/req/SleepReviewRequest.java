package com.umc_9th.sleepinghero.domain.sleep.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SleepReviewRequest(
        Long recordId,

        @Min(1) @Max(5)
        int star,
        String comment
) {
}
