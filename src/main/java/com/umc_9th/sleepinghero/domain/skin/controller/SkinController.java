package com.umc_9th.sleepinghero.domain.skin.controller;

import com.umc_9th.sleepinghero.domain.skin.dto.res.SkinResponseDTO;
import com.umc_9th.sleepinghero.domain.skin.service.SkinService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SkinController {

    private final SkinService skinService;

    @GetMapping("/wardrobe/me/skins")
    @Operation(summary = "보유 스킨 목록 조회 API", description = "내가 가진 스킨들과 현재 착용 여부를 조회합니다.")
    public ApiResponse<SkinResponseDTO.SkinListDTO> getMySkins(
            @Parameter(hidden = true)  @AuthenticationPrincipal Long memberId
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, skinService.getMySkins(memberId));
    }
    @PostMapping("/wardrobe/me/skins/{skinId}/equip")
    @Operation(summary = "스킨 착용 API", description = "보유한 스킨 중 하나를 골라 캐릭터에 입힙니다.")
    public ApiResponse<String> equipSkin(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId,
            @PathVariable Long skinId
    ) {
        skinService.equipSkin(memberId, skinId);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, "스킨 착용이 완료되었습니다.");
    }
}


