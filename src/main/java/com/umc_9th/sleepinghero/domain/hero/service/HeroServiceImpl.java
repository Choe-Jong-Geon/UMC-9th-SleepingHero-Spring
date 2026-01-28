package com.umc_9th.sleepinghero.domain.hero.service;

import com.umc_9th.sleepinghero.domain.hero.dto.req.HeroRequestDTO;
import com.umc_9th.sleepinghero.domain.hero.dto.res.HeroResponseDTO;
import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.hero.util.LevelPolicy;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.skin.entity.Skin;
import com.umc_9th.sleepinghero.domain.skin.exception.SkinErrorCode;
import com.umc_9th.sleepinghero.domain.skin.repository.SkinRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final SkinRepository skinRepository;
    private final MemberRepository memberRepository;


    @Override
    public HeroResponseDTO.HeroDetailDTO getHeroDetail(Long memberId) {

        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));


        return HeroResponseDTO.HeroDetailDTO.builder()
                .heroId(hero.getId())
                .name(hero.getName())
                .currentLevel(hero.getCurrentLevel())
                .currentExp(hero.getCurrentExp())
                .needExp(LevelPolicy.needExp(hero.getCurrentLevel()))
                .currentStage(hero.getCurrentStage())
                .build();
    }


    @Override
    @Transactional
    public HeroResponseDTO.HeroDetailDTO createDefaultHero(Long memberId) {

        if (heroRepository.findByMemberId(memberId).isPresent()) {
            throw new GeneralException(HeroErrorCode.ALREADY_EXIST_HERO);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));


        Skin defaultSkin = skinRepository.findById(1L)
                .orElseThrow(() -> new GeneralException(SkinErrorCode.SKIN_NOT_FOUND));


        Hero newHero = Hero.builder()
                .name("김용사")
                .member(member)
                .currentLevel(1)
                .currentSkin(defaultSkin)
                .currentExp(0)
                .currentStage(1)
                .build();

        Hero savedHero = heroRepository.save(newHero);
        return HeroResponseDTO.toDetailDTO(savedHero);
    }

    @Override
    @Transactional
    public HeroResponseDTO.HeroDetailDTO updateHeroName(Long memberId, HeroRequestDTO.UpdateNameDTO request) {

        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));


        hero.updateName(request.getName());

        return HeroResponseDTO.toDetailDTO(hero);
    }
}