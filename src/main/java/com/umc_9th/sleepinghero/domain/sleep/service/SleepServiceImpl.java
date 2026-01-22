package com.umc_9th.sleepinghero.domain.sleep.service;


import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.sleep.converter.SleepConverter;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepEndResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepStartResponse;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.exception.SleepErrorCode;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SleepServiceImpl implements SleepService {

    private final SleepRecordRepository sleepRecordRepository;
    private final SleepGoalRepository sleepGoalRepository;
    private final MemberRepository memberRepository;

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
    public SleepRecordResponse getSleepRecord(Long sleepRecordId, Long memberId) {
        SleepRecord record = sleepRecordRepository.findByIdAndMemberId(sleepRecordId, memberId)
                .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_RECORD_NOT_FOUND));

        return sleepConverter.toDto(record);
    }


    @Override
    @Transactional
    public SleepStartResponse startSleep(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        SleepRecord record = SleepRecord.builder()
                .sleptTime(LocalDateTime.now().withNano(0))
                .member(member)
                .build();

        sleepRecordRepository.save(record);

        member.startSleep();

        return sleepConverter.toDto(record,member.isSleepStatus());
    }


    @Override
    @Transactional
    public SleepEndResponse endSleep(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        if(!member.isSleepStatus())
            throw new GeneralException(SleepErrorCode.SLEEP_RECORD_BAD_REQUEST);

        SleepRecord record = sleepRecordRepository
                .findTopByMemberIdAndWokeTimeIsNullOrderBySleptTimeDesc(memberId)
                .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_RECORD_NOT_FOUND));

        SleepGoal goal = sleepGoalRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        LocalDateTime now = LocalDateTime.now().withNano(0);
        LocalDateTime goalDateTime = LocalDateTime.of(now.toLocalDate(), goal.getWakeTime());

        record.updateWokeTime(now);

        if(now.isBefore(goalDateTime))
            goal.successGoal();
        else
            goal.failGoal();

        member.endSleep();

        Duration d = Duration.between(record.getSleptTime(), record.getWokeTime());

        Long hours = d.toHours();
        Long minutes = d.toMinutes() % 60;

        return sleepConverter.toDto(record, hours, minutes, 10, 1);
    }
}
