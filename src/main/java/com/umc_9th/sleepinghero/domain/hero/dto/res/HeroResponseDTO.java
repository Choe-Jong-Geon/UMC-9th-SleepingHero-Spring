package com.umc_9th.sleepinghero.domain.hero.dto.res;

import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.util.LevelPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HeroResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeroDetailDTO {
        private Long heroId;
        private String name;
        private int currentLevel;
        private int currentExp;
        private int needExp;
        private int currentStage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchHeroResultDTO {
        private Long memberId;
        private Long heroId;
        private String heroName;
        private int level;
        private Long skinId;
        private int continuousSleepDays;
        private int totalSleepHour;
    }


    public static HeroDetailDTO toDetailDTO(Hero hero) {
        return HeroDetailDTO.builder()
                .heroId(hero.getId())
                .name(hero.getName())
                .currentLevel(hero.getCurrentStage())
                .currentExp(hero.getCurrentExp())
                .needExp(LevelPolicy.needExp(hero.getCurrentLevel()))
                .currentStage(hero.getCurrentStage())
                .build();
    }



}