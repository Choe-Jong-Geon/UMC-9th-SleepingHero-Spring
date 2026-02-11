package com.umc_9th.sleepinghero.domain.hero.service;

import com.umc_9th.sleepinghero.domain.hero.dto.req.HeroRequestDTO;
import com.umc_9th.sleepinghero.domain.hero.dto.res.HeroResponseDTO;
import com.umc_9th.sleepinghero.domain.member.entity.Member;

public interface HeroService {
    HeroResponseDTO.HeroDetailDTO getHeroDetail(Long memberId);

    HeroResponseDTO.HeroDetailDTO createDefaultHero(Long memberId);

    HeroResponseDTO.HeroDetailDTO updateHeroName(Long memberId, HeroRequestDTO.UpdateNameDTO request);

    HeroResponseDTO.SearchHeroResultDTO searchHeroByName(String name);

    void checkAndUnlockSkin(Member member, int currentLevel);

}


