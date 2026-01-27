package com.umc_9th.sleepinghero.domain.skin.service;

import com.umc_9th.sleepinghero.domain.skin.dto.res.SkinResponseDTO;

public interface SkinService {

    SkinResponseDTO.SkinListDTO getMySkins(Long memberId);

    void equipSkin(Long memberId, Long skinId);
}
