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
public class HelpController implements HelpControllerDocs {

    private final HelpService helpService;

    @PostMapping("/inquiry")
    public ApiResponse<String> createInquiry(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid HelpRequestDTO.CreateHelpDTO request
    ) {
        helpService.createHelpInquiry(memberId, request);

        return ApiResponse.onSuccess(GeneralSuccessCode.OK, "문의가 성공적으로 접수되었습니다.");
    }
}