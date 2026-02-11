package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepReviewResponse;

public interface SleepReviewService {
    SleepReviewResponse createReview(SleepReviewRequest request, Long memberId);

}
