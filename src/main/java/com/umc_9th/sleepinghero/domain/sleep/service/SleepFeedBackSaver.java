package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepFeedBackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SleepFeedBackSaver {

    private final SleepFeedBackRepository sleepFeedBackRepository;

    @Transactional
    public void save(AiSleepFeedBack feedBack, SleepReview review) {

        SleepFeedBack entity = SleepFeedBack.builder()
                .summary(feedBack.summary())
                .cheering(feedBack.cheering())
                .sleepReview(review)
                .build();

        feedBack.improvements()
                .forEach(entity::addImprovement);

        feedBack.positives()
                .forEach(entity::addPositive);

        sleepFeedBackRepository.save(entity);
    }
}
