package com.umc_9th.sleepinghero.domain.hero.converter;

import com.umc_9th.sleepinghero.domain.hero.dto.res.HeroResponseDTO;
import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.util.LevelPolicy;

public class HeroConverter {

    // Hero 엔티티 -> HeroDetailDTO 변환
    public static HeroResponseDTO.HeroDetailDTO toHeroDetailDTO(Hero hero) {
        return HeroResponseDTO.HeroDetailDTO.builder()
                .heroId(hero.getId())
                .name(hero.getName())
                .currentLevel(hero.getCurrentLevel())
                .currentExp(hero.getCurrentExp())
                .needExp(LevelPolicy.needExp(hero.getCurrentLevel()))
                .currentStage(hero.getCurrentStage())
                .build();
    }

    // Hero 엔티티 -> SearchHeroResultDTO 변환
    public static HeroResponseDTO.SearchHeroResultDTO toSearchHeroResultDTO(Hero hero, int streak, int totalHours) {
        return HeroResponseDTO.SearchHeroResultDTO.builder()
                .memberId(hero.getMember().getId())
                .heroId(hero.getId())
                .heroName(hero.getName())
                .level(hero.getCurrentLevel())
                .skinId(hero.getCurrentSkin().getId())
                .continuousSleepDays(streak)
                .totalSleepHour(totalHours)
                .build();
    }
}