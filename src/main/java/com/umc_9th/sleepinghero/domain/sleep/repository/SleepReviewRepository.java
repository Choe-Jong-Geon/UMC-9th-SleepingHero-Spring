package com.umc_9th.sleepinghero.domain.sleep.repository;

import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SleepReviewRepository extends JpaRepository<SleepReview, Long> {
    List<SleepReview> findAllBySleepRecordIn(List<SleepRecord> records);

    Optional<SleepReview> findBySleepRecordId(Long sleepRecordId);

    void deleteAllBySleepRecordIn(List<SleepRecord> records);
}
