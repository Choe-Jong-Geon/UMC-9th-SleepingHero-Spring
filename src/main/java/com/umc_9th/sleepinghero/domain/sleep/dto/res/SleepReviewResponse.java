package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import java.util.List;

public record SleepReviewResponse (
        long reviewId,
        String summary,
        List<String> positives,
        List<String> improvements,
        String cheering
){
}
