package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import com.umc_9th.sleepinghero.domain.hero.dto.res.LevelChange;

public record SleepReward (
        int gainedExp,
        boolean isDeBuff,
        LevelChange levelChange
){
}
