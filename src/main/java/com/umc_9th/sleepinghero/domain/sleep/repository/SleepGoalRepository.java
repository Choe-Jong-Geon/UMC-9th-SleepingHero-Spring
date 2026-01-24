package com.umc_9th.sleepinghero.domain.sleep.repository;

import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SleepGoalRepository extends JpaRepository<SleepGoal, Long> {
    Optional<SleepGoal> findByMemberId(Long memberId);
}
