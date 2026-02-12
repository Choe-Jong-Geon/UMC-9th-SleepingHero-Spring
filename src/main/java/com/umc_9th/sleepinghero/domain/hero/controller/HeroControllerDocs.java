package com.umc_9th.sleepinghero.domain.hero.controller;

import com.umc_9th.sleepinghero.domain.hero.dto.req.HeroRequestDTO;
import com.umc_9th.sleepinghero.domain.hero.dto.res.HeroResponseDTO;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "hero-controller", description = "캐릭터(용사) 관련 API")
public interface HeroControllerDocs {

    @Operation(summary = "캐릭터 상세 조회 API", description = "로그인한 유저의 캐릭터(용사) 정보를 조회합니다.")
    ApiResponse<HeroResponseDTO.HeroDetailDTO> getHeroDetail( @Parameter(hidden = true) Long memberId);

    @Operation(summary = "캐릭터 자동 생성 API", description = "튜토리얼 완료 후 기본 이름(김용사)으로 캐릭터를 생성합니다.")
    ApiResponse<HeroResponseDTO.HeroDetailDTO> createDefaultHero( @Parameter(hidden = true) Long memberId);

    @Operation(summary = "캐릭터 이름 변경 API", description = "사용자가 원하는 이름으로 캐릭터 이름을 변경합니다.")
    ApiResponse<HeroResponseDTO.HeroDetailDTO> updateHeroName( @Parameter(hidden = true) Long memberId, @Valid @RequestBody HeroRequestDTO.UpdateNameDTO request);

    @Operation(summary = "친구 탐색(용사 검색) API", description = "이름으로 다른 유저의 용사 정보를 조회합니다.")
    ApiResponse<HeroResponseDTO.SearchHeroResultDTO> searchHero( @Parameter(hidden = true) Long memberId, @RequestParam(name = "name") String name);
}
