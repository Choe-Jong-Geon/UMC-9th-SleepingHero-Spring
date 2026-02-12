package com.umc_9th.sleepinghero.domain.skin.service;

import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.skin.converter.SkinConverter;
import com.umc_9th.sleepinghero.domain.skin.dto.res.SkinResponseDTO;
import com.umc_9th.sleepinghero.domain.skin.entity.Skin;
import com.umc_9th.sleepinghero.domain.skin.entity.SkinMember;
import com.umc_9th.sleepinghero.domain.skin.exception.SkinErrorCode;
import com.umc_9th.sleepinghero.domain.skin.repository.SkinMemberRepository;
import com.umc_9th.sleepinghero.domain.skin.repository.SkinRepository;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkinServiceImpl implements SkinService {

    private final SkinMemberRepository skinMemberRepository; // 이름 수정
    private final HeroRepository heroRepository;
    private final SkinRepository skinRepository;

    @Override
    public SkinResponseDTO.SkinListDTO getMySkins(Long memberId) {

        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));

        List<SkinMember> mySkins = skinMemberRepository.findAllByMemberId(memberId);

        return SkinConverter.toSkinListDTO(mySkins, hero.getCurrentSkin().getId());

    }

    @Override
    @Transactional
    public void equipSkin(Long memberId, Long skinId) {

        skinMemberRepository.findByMemberIdAndSkinId(memberId, skinId)
                .orElseThrow(() -> new GeneralException(SkinErrorCode.SKIN_NOT_OWNED));


        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));

        Skin skinToEquip = skinRepository.findById(skinId)
                .orElseThrow(() -> new GeneralException(SkinErrorCode.SKIN_NOT_FOUND));


        hero.updateSkin(skinToEquip);
    }
}
