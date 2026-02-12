package com.umc_9th.sleepinghero.domain.help.controller;

import com.umc_9th.sleepinghero.domain.help.dto.req.HelpRequestDTO;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "help-controller", description = "도움말 및 고객 문의 관련 API")
public interface HelpControllerDocs {

    @Operation(summary = "문의하기 등록 API", description = "버그 신고 및 불편 사항을 직접 입력받아 저장합니다.")
    ApiResponse<String> createInquiry(@Parameter(hidden = true) Long memberId, @RequestBody @Valid HelpRequestDTO.CreateHelpDTO request);
}