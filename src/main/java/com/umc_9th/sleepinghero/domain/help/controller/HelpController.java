package com.umc_9th.sleepinghero.domain.help.controller;

import com.umc_9th.sleepinghero.domain.help.dto.req.HelpRequestDTO;
import com.umc_9th.sleepinghero.domain.help.dto.res.HelpResponseDTO;
import com.umc_9th.sleepinghero.domain.help.service.HelpService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/help")
public class HelpController {

    private final HelpService helpService;

    @PostMapping("/inquiry")
    @Operation(summary = "문의하기 등록 API", description = "버그 신고 및 불편 사항을 직접 입력받아 저장합니다.")
    public ApiResponse<String> createInquiry(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid HelpRequestDTO.CreateHelpDTO request
    ) {
        helpService.createHelpInquiry(memberId, request);

        return ApiResponse.onSuccess(GeneralSuccessCode.OK, "문의가 성공적으로 접수되었습니다.");
    }
}