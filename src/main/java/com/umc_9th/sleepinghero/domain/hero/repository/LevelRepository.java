package com.umc_9th.sleepinghero.domain.hero.repository;

import com.umc_9th.sleepinghero.domain.hero.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

}