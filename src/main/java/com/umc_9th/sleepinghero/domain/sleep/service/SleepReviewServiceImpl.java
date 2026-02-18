package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.converter.SleepConverter;
import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepReviewResponse;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import com.umc_9th.sleepinghero.domain.sleep.exception.SleepErrorCode;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepReviewRepository;
import com.umc_9th.sleepinghero.domain.sleep.util.SleepTimeCalculator;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SleepReviewServiceImpl implements SleepReviewService {

    private final SleepReviewRepository sleepReviewRepository;
    private final SleepRecordRepository sleepRecordRepository;
    private final SleepGoalRepository sleepGoalRepository;
    private final SleepFeedBackService sleepFeedBackService;
    private final SleepConverter sleepConverter;

    @Override
    public SleepReviewResponse createReview(
            SleepReviewRequest request,
            Long memberId
    ) {

        // 1️⃣ 리뷰 먼저 저장 (commit 발생)
        SleepReview review = createReviewEntity(request, memberId);

        SleepRecord record = review.getSleepRecord();
        SleepGoal goal = sleepGoalRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new GeneralException(SleepErrorCode.SLEEP_GOAL_NOT_FOUND));

        long sleepDuration =
                Duration.between(record.getSleptTime(), record.getWokeTime()).toMinutes();

        long goalDuration =
                SleepTimeCalculator.durationMinutes(
                        goal.getSleepTime(),
                        goal.getWakeTime()
                );

        // 2️⃣ AI 호출 (트랜잭션 없음)
        AiSleepFeedBack feedBack =
                sleepFeedBackService.feedback(
                        sleepDuration,
                        goalDuration,
                        goal.getCurrentStreak(),
                        review
                );

        return sleepConverter.toDto(review.getId(), feedBack);
    }


    @Transactional
    public SleepReview createReviewEntity(SleepReviewRequest request, Long memberId) {

        SleepRecord record = sleepRecordRepository
                .findByIdAndMemberId(request.recordId(), memberId)
                .orElseThrow(() ->
                        new GeneralException(SleepErrorCode.SLEEP_RECORD_NOT_FOUND));

        SleepGoal goal = sleepGoalRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new GeneralException(SleepErrorCode.SLEEP_GOAL_NOT_FOUND));

        SleepReview review = SleepReview.builder()
                .star(request.star())
                .comment(request.comment())
                .sleepRecord(record)
                .build();

        return sleepReviewRepository.save(review);
    }

}
