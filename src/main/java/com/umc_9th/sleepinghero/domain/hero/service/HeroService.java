package com.umc_9th.sleepinghero.domain.hero.service;

import com.umc_9th.sleepinghero.domain.hero.dto.req.HeroRequestDTO;
import com.umc_9th.sleepinghero.domain.hero.dto.res.HeroResponseDTO;

public interface HeroService {
    HeroResponseDTO.HeroDetailDTO getHeroDetail(Long memberId);


    HeroResponseDTO.HeroDetailDTO createDefaultHero(Long memberId);


    HeroResponseDTO.HeroDetailDTO updateHeroName(Long memberId, HeroRequestDTO.UpdateNameDTO request);
}