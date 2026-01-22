package com.umc_9th.sleepinghero.domain.home.converter;

import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.home.dto.res.DashBoardResponse;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import org.springframework.stereotype.Component;

@Component
public class HomeConverter {


    public DashBoardResponse toDto(Hero hero, SleepGoal goal){
        return new DashBoardResponse(
                hero.getId(),
                hero.getCurrentStage(),
                goal.getCurrentStreak()
        );
    }
}
