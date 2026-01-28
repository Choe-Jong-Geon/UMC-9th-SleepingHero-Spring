package com.umc_9th.sleepinghero.domain.hero.dto.res;

public record LevelChange (
    int prevLevel,          // 이전 레벨
    int currentLevel,       // 현재 레벨
    int currentExp,         // 현재 경험치
    int needExp         // 다음 레벨로 가기 위한 총 경험치
)
{}
