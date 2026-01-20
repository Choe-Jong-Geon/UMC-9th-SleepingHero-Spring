package com.umc_9th.sleepinghero.domain.sleep.converter;

import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

//    public SleepRecord toEntity(SleepRecordResponse sleepRecordResponse) {
//        return SleepRecord.builder()
//                .
//                .build();
//    }
}
