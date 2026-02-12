package com.umc_9th.sleepinghero.domain.hero.controller;

import com.umc_9th.sleepinghero.domain.hero.dto.req.HeroRequestDTO;
import com.umc_9th.sleepinghero.domain.hero.dto.res.HeroResponseDTO;
import com.umc_9th.sleepinghero.domain.hero.service.HeroService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
public class HeroController implements HeroControllerDocs {

    private final HeroService heroService;

    @GetMapping("/me")
    public ApiResponse<HeroResponseDTO.HeroDetailDTO> getHeroDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId
    ) {

        HeroResponseDTO.HeroDetailDTO result = heroService.getHeroDetail(memberId);

        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


    @PostMapping("")
    public ApiResponse<HeroResponseDTO.HeroDetailDTO> createDefaultHero(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, heroService.createDefaultHero(memberId));
    }

    @PatchMapping("/name")
    public ApiResponse<HeroResponseDTO.HeroDetailDTO> updateHeroName(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody HeroRequestDTO.UpdateNameDTO request
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, heroService.updateHeroName(memberId, request));
    }

    @GetMapping("/search")
    public ApiResponse<HeroResponseDTO.SearchHeroResultDTO> searchHero(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId,
            @RequestParam(name = "name") String name
    ) {
        HeroResponseDTO.SearchHeroResultDTO result = heroService.searchHeroByName(name);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}