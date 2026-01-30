package com.umc_9th.sleepinghero.domain.hero.util;

import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;

public final class LevelPolicy {

    private static final int MIN = 20;
    private static final int MID = 55;
    private static final int MAX = 100;

    private static final int BASE = 100;

    private static final int MIN_INC = 20; // 2~20
    private static final int MID_INC = 30; // 21~55
    private static final int MAX_INC = 40; // 56~100

    private LevelPolicy() {}

    public static int needExp(int level) {
        if (level < 1)
            throw new GeneralException(HeroErrorCode.INVALID_LEVEL_STATE);
        if (level >= MAX) return 0;

        if (level == 1) return BASE;

        int req = BASE;

        // 2 ~ 20
        int minPart = Math.min(level, MIN) - 1;
        req += minPart * MIN_INC;

        // 21 ~ 55
        if (level > MIN) {
            int midPart = Math.min(level, MID) - MIN;
            req += midPart * MID_INC;
        }

        // 56 ~ 100
        if (level > MID) {
            int maxPart = level - MID;
            req += maxPart * MAX_INC;
        }

        return req;
    }

    public static int getMax(){ return MAX; }
}
