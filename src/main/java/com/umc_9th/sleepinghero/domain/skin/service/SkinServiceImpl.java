package com.umc_9th.sleepinghero.domain.skin.service;

import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
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
        // 1. 현재 장착 스킨 확인용 Hero 조회
        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));

        Long equippedSkinId = hero.getCurrentSkin().getId();

        // 2. 보유 스킨 목록(SkinMember) 조회
        List<SkinMember> mySkins = skinMemberRepository.findAllByMemberId(memberId);

        // 3. DTO 변환
        List<SkinResponseDTO.SkinInfoDTO> skinInfoList = mySkins.stream()
                .map(sm -> {
                    Skin skin = sm.getSkin();
                    return SkinResponseDTO.SkinInfoDTO.builder()
                            .skinId(skin.getId())
                            .name(skin.getName())
                            .isEquipped(skin.getId().equals(equippedSkinId))
                            .build();
                })
                .collect(Collectors.toList());

        return SkinResponseDTO.SkinListDTO.builder()
                .skins(skinInfoList)
                .build();
    }

    @Override
    @Transactional
    public void equipSkin(Long memberId, Long skinId) {
        // 1. 보유 여부 검증 (SkinMember 테이블 확인)
        skinMemberRepository.findByMemberIdAndSkinId(memberId, skinId)
                .orElseThrow(() -> new GeneralException(SkinErrorCode.SKIN_NOT_OWNED));

        // 2. 캐릭터 및 바꿀 스킨 엔티티 조회
        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));

        Skin skinToEquip = skinRepository.findById(skinId)
                .orElseThrow(() -> new GeneralException(SkinErrorCode.SKIN_NOT_FOUND));

        // 3. 스킨 교체
        hero.updateSkin(skinToEquip);
    }
}
