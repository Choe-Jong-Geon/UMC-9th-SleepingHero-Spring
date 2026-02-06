package com.umc_9th.sleepinghero.domain.sleep.converter;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBackResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.*;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import org.springframework.stereotype.Component;

@Component
public class SleepConverter {

    public SleepRecordResponse toDto(SleepRecord sleepRecord) {
        return new SleepRecordResponse(
          sleepRecord.getId(),
            sleepRecord.getSleptTime(),
            sleepRecord.getWokeTime(),
            sleepRecord.isSuccess()
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

    public SleepReviewResponse toDto(long id, AiSleepFeedBackResponse feedBack){
        return new SleepReviewResponse(
                id, feedBack
        );
    }
}
