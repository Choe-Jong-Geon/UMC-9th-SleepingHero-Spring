package com.umc_9th.sleepinghero.domain.sleep.util;

import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;

import java.time.Duration;

public class SleepRewardPolicy {

    public static int calculateGainedExp(Duration d, SleepGoal goal) {

        long m = d.toMinutes();

        int baseExp;
        if (m <= 4 * 60) baseExp = 0;
        else if (m <= 5 * 60) baseExp = 20;
        else if (m <= 6 * 60) baseExp = 40;
        else if (m <= 7 * 60) baseExp = 60;
        else if (m <= 8 * 60) baseExp = 80;
        else baseExp = 100;

        boolean debuff = goal != null && goal.getNonSleepStreak() >= 3;

        if (!debuff) return baseExp;

        return (int) Math.round(baseExp * 0.8);
    }
}
