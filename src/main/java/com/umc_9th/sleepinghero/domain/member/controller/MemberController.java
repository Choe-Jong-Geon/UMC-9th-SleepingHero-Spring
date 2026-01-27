package com.umc_9th.sleepinghero.domain.member.controller;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponseDTO;
import com.umc_9th.sleepinghero.domain.member.service.MemberService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/me/tutorial")
    @Operation(summary = "튜토리얼 조회 API", description = "유저의 튜토리얼 완료 여부를 조회합니다.")
    public ApiResponse<MemberResponseDTO.CheckTutorialDTO> checkTutorial(@AuthenticationPrincipal Long memberId
    ) {
        MemberResponseDTO.CheckTutorialDTO result = memberService.checkTutorial(memberId);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


    @PatchMapping("/me/tutorial")
    @Operation(summary = "튜토리얼 완료 처리 API", description = "유저의 튜토리얼 상태를 변경합니다.")
    public ApiResponse<MemberResponseDTO.CompleteTutorialResultDTO> completeTutorial(
            @AuthenticationPrincipal Long memberId,
            @RequestBody MemberRequestDTO.CompleteTutorialDTO request
    ) {
        MemberResponseDTO.CompleteTutorialResultDTO result = memberService.completeTutorial(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


    @GetMapping("/me/agreements")
    @Operation(summary = "약관 동의 내역 조회 API", description = "마케팅 정보 수신 동의 여부 등을 조회합니다.")
    public ApiResponse<MemberResponseDTO.AgreementsDTO> getAgreements(
            @AuthenticationPrincipal Long memberId
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, memberService.getAgreements(memberId));
    }


    @PatchMapping("/me/agreements")
    @Operation(summary = "마케팅 수신 동의 변경 API", description = "마케팅 정보 수신 동의 여부를 변경합니다.")
    public ApiResponse<MemberResponseDTO.AgreementsDTO> updateAgreements(
            @AuthenticationPrincipal Long memberId,
            @RequestBody MemberRequestDTO.UpdateAgreementsDTO request
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, memberService.updateAgreements(memberId, request));
    }
}
