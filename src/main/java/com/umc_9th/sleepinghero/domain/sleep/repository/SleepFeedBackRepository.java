package com.umc_9th.sleepinghero.domain.sleep.repository;

import com.umc_9th.sleepinghero.domain.sleep.entity.SleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepFeedBackRepository
        extends JpaRepository<SleepFeedBack, Long> {

    boolean existsBySleepReview(SleepReview sleepReview);
}
