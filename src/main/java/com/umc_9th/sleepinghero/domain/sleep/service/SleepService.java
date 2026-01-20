package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface SleepService {

    // 수면 기록 조회 로직
    Page<SleepRecordResponse> getSleepRecords(int page, int size, Long memberId);
    SleepRecordResponse getSleepRecord(Long id,Long memberId);

    //
}
