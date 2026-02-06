package com.umc_9th.sleepinghero.domain.sleep.repository;

import com.umc_9th.sleepinghero.domain.sleep.entity.SleepFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SleepFeedBackRepository extends JpaRepository<SleepFeedBack, Long> {
}
