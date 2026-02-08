package com.umc_9th.sleepinghero.domain.help.repository;

import com.umc_9th.sleepinghero.domain.help.entity.Help;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpRepository extends JpaRepository<Help, Long> {
}


