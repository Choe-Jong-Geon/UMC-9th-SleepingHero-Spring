package com.umc_9th.sleepinghero.domain.sleep.converter;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.*;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import org.springframework.stereotype.Component;

@Component
public class SleepConverter {

    public SleepRecordResponse toDto(SleepRecord record, SleepFeedBack feedBack, SleepReview review, long minutes) {
        return new SleepRecordResponse(
            record.getId(),
                review.getStar(),
                record.getSleptTime().toLocalDate(),
                minutes,
                feedBack.getSummary(),
                feedBack.getPositives(),
                feedBack.getImprovements(),
                feedBack.getCheering()
        );
    }

    public SleepStartResponse toDto(SleepRecord sleepRecord, boolean sleepStatus) {
        return new SleepStartResponse(
                sleepRecord.getId(),
                sleepRecord.getSleptTime(),
                sleepStatus
        );
    }

    public SleepEndResponse toDto(
            SleepRecord sleepRecord, long durationMinutes,
            SleepReward reward, int currentStage
    ) {
        return new SleepEndResponse(
                sleepRecord.getId(),
                sleepRecord.getSleptTime(),
                sleepRecord.getSleptTime(),
                durationMinutes,
                reward,
                currentStage
        );
    }

    public SleepReviewResponse toDto(Long reviewId, AiSleepFeedBack feedBack) {
        return new SleepReviewResponse(
                reviewId,
                feedBack.summary(),
                feedBack.positives(),
                feedBack.improvements(),
                feedBack.cheering()
        );
    }
}
