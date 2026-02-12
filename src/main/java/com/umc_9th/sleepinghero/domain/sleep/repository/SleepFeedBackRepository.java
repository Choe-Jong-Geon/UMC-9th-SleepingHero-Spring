package com.umc_9th.sleepinghero.domain.sleep.repository;

import com.umc_9th.sleepinghero.domain.sleep.entity.SleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SleepFeedBackRepository extends JpaRepository<SleepFeedBack, Long> {
    boolean existsBySleepReview(SleepReview sleepReview);
    void deleteAllBySleepReviewIn(List<SleepReview> reviews);
    Optional<SleepFeedBack> findBySleepReviewId(long sleepReviewId);
}
