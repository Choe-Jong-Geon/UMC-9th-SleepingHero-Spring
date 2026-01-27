package com.umc_9th.sleepinghero.domain.help.controller;

import com.umc_9th.sleepinghero.domain.help.dto.res.HelpResponseDTO;
import com.umc_9th.sleepinghero.domain.help.service.HelpService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/help")
public class HelpController {


    private final HelpService helpService;

    @GetMapping("/inquiries")
    @Operation(summary = "문의하기 링크 조회 API", description = "문의하기 클릭 시 이동할 외부 링크(구글 폼 등)를 반환합니다.")
    public ApiResponse<HelpResponseDTO.InquiryUrlDTO> getInquiryUrl(

            @AuthenticationPrincipal Long memberId
    ) {

        HelpResponseDTO.InquiryUrlDTO result = helpService.getInquiryUrl();

        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}
