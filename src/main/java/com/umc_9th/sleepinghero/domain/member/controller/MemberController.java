package com.umc_9th.sleepinghero.domain.member.controller;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponseDTO;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.service.MemberService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users") // 기본 URL: /users
public class MemberController {

    private final MemberService memberService; // 인터페이스를 가져옵니다!


    @GetMapping("/me/tutorial")
    @Operation(summary = "튜토리얼 조회 API", description = "유저의 튜토리얼 완료 여부를 조회합니다.")
    public ApiResponse<MemberResponseDTO.CheckTutorialDTO> checkTutorial(
            // TODO: 나중에 토큰에서 유저 정보를 가져오는 어노테이션(@CurrentUser 등)으로 교체해야 합니다.
            // 지금은 임시로 코드가 돌아가게만 둡니다.
            Member member
    ) {
        MemberResponseDTO.CheckTutorialDTO result = memberService.checkTutorial(member);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


    @PatchMapping("/me/tutorial")
    @Operation(summary = "튜토리얼 완료 처리 API", description = "유저의 튜토리얼 상태를 변경합니다.")
    public ApiResponse<MemberResponseDTO.CompleteTutorialResultDTO> completeTutorial(
            // TODO: 여기도 마찬가지로 인증된 유저 객체가 필요합니다.
            Member member,
            @RequestBody MemberRequestDTO.CompleteTutorialDTO request
    ) {
        MemberResponseDTO.CompleteTutorialResultDTO result = memberService.completeTutorial(member, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


    @GetMapping("/me/agreements")
    @Operation(summary = "약관 동의 내역 조회 API", description = "마케팅 정보 수신 동의 여부 등을 조회합니다.")
    public ApiResponse<MemberResponseDTO.AgreementsDTO> getAgreements(
            Member member // @CurrentUser 등 적용 예정
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, memberService.getAgreements(member));
    }


    @PatchMapping("/me/agreements")
    @Operation(summary = "마케팅 수신 동의 변경 API", description = "마케팅 정보 수신 동의 여부를 변경합니다.")
    public ApiResponse<MemberResponseDTO.AgreementsDTO> updateAgreements(
            Member member, // @CurrentUser 등 적용 예정
            @RequestBody MemberRequestDTO.UpdateAgreementsDTO request
    ) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, memberService.updateAgreements(member, request));
    }
}
