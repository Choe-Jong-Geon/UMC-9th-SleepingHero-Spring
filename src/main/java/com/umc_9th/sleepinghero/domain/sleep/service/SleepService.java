package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepEndResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepReviewResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepStartResponse;
import org.springframework.data.domain.Page;

public interface SleepService {

    // 수면 기록 조회 로직
    Page<SleepRecordResponse> getSleepRecords(int page, int size, Long memberId);
    SleepRecordResponse getSleepRecord(Long sleepRecordId,Long memberId);

    SleepStartResponse startSleep(Long memberId);

    SleepEndResponse endSleep(Long memberId);

    SleepReviewResponse createReview(SleepReviewRequest request, Long memberId);

    long testRecord(Long memberId);

}
