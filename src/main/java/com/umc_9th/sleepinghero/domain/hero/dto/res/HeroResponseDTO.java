package com.umc_9th.sleepinghero.domain.hero.dto.res;

import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
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
        private int level;
        private double currentExp;
        private int needExp;
        private int currentStage;
    }

    public static HeroDetailDTO toDetailDTO(Hero hero) {
        return HeroDetailDTO.builder()
                .heroId(hero.getId())
                .name(hero.getName())
                .level(hero.getLevel().getId().intValue())
                .currentExp(hero.getCurrentExp())
                .needExp(hero.getLevel().getNeedExp())
                .currentStage(hero.getCurrentStage())
                .build();
    }

}