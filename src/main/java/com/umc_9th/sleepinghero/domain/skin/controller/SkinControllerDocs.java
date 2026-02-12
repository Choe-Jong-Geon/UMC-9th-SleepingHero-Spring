package com.umc_9th.sleepinghero.domain.skin.controller;

import com.umc_9th.sleepinghero.domain.skin.dto.res.SkinResponseDTO;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "skin-controller", description = "스킨(외형) 조회 및 장착 관련 API")
public interface SkinControllerDocs {

    @Operation(summary = "보유 스킨 목록 조회 API", description = "내가 가진 스킨들과 현재 착용 여부를 조회합니다.")
    ApiResponse<SkinResponseDTO.SkinListDTO> getMySkins( @Parameter(hidden = true) Long memberId);

    @Operation(summary = "스킨 착용 API", description = "보유한 스킨 중 하나를 골라 캐릭터에 입힙니다.")
    ApiResponse<String> equipSkin( @Parameter(hidden = true) Long memberId, @PathVariable Long skinId);
}