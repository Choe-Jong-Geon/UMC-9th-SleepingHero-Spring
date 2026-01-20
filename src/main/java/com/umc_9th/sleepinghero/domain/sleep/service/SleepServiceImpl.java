package com.umc_9th.sleepinghero.domain.sleep.service;


import com.umc_9th.sleepinghero.domain.sleep.converter.SleepConverter;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.exception.SleepErrorCode;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SleepServiceImpl implements SleepService {

    private final SleepRecordRepository sleepRecordRepository;
    private final SleepConverter sleepConverter;


    @Override
    @Transactional(readOnly = true)
    public Page<SleepRecordResponse> getSleepRecords(int page, int size, Long memberId) {

        PageRequest request = PageRequest.of(page, size);

        Page<SleepRecord> records = sleepRecordRepository.findByMemberId(
                memberId,request
        );

        return records.map(sleepConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public SleepRecordResponse getSleepRecord(Long id, Long memberId) {
        SleepRecord record = sleepRecordRepository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_RECORD_NOT_FOUND));

        return sleepConverter.toDto(record);
    }
}
