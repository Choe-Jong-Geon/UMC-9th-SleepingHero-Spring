package com.umc_9th.sleepinghero.domain.skin.converter;

import com.umc_9th.sleepinghero.domain.skin.dto.res.SkinResponseDTO;
import com.umc_9th.sleepinghero.domain.skin.entity.SkinMember;

import java.util.List;
import java.util.stream.Collectors;

public class SkinConverter {

    // 개별 스킨 정보 변환
    public static SkinResponseDTO.SkinInfoDTO toSkinInfoDTO(SkinMember skinMember, Long equippedSkinId) {
        return SkinResponseDTO.SkinInfoDTO.builder()
                .skinId(skinMember.getSkin().getId())
                .name(skinMember.getSkin().getName())
                .isEquipped(skinMember.getSkin().getId().equals(equippedSkinId))
                .build();
    }

    // 스킨 리스트 전체 변환
    public static SkinResponseDTO.SkinListDTO toSkinListDTO(List<SkinMember> skinMemberList, Long equippedSkinId) {
        List<SkinResponseDTO.SkinInfoDTO> skinInfoDTOList = skinMemberList.stream()
                .map(sm -> toSkinInfoDTO(sm, equippedSkinId))
                .collect(Collectors.toList());

        return SkinResponseDTO.SkinListDTO.builder()
                .skins(skinInfoDTOList)
                .build();
    }
}