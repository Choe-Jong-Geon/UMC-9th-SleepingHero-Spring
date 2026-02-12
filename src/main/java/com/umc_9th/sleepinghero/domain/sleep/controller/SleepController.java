package com.umc_9th.sleepinghero.domain.sleep.controller;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepEndResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepReviewResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepStartResponse;
import com.umc_9th.sleepinghero.domain.sleep.service.SleepReviewService;
import com.umc_9th.sleepinghero.domain.sleep.service.SleepService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sleep-sessions")
@RequiredArgsConstructor
@Tag(name = "Sleep", description = "수면 세션 및 리뷰 관리 API")
public class SleepController {

    private final SleepService sleepService;
    private final SleepReviewService sleepReviewService;

    @Operation(
            summary = "수면 기록 목록 조회",
            description = "로그인한 사용자의 수면 기록을 페이지 단위로 조회합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SleepRecordResponse>>> getSleepRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(hidden = true)
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                sleepService.getSleepRecords(page, size, memberId)
        ));
    }

    @Operation(
            summary = "수면 기록 단건 조회",
            description = "특정 수면 기록 ID에 해당하는 상세 정보를 조회합니다."
    )
    @GetMapping("/{sleepRecordId}")
    public ResponseEntity<ApiResponse<SleepRecordResponse>> getSleepRecord(
            @PathVariable Long sleepRecordId,
            @Parameter(hidden = true)
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                sleepService.getSleepRecord(sleepRecordId, memberId)
        ));
    }

    @Operation(
            summary = "수면 시작",
            description = "현재 시간을 기준으로 수면 세션을 시작합니다. 이미 수면 중인 경우 오류가 발생합니다."
    )
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<SleepStartResponse>> startSleep(
            @Parameter(hidden = true)
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                sleepService.startSleep(memberId)
        ));
    }

    @Operation(
            summary = "수면 종료",
            description = "현재 진행 중인 수면 세션을 종료하고, 수면 시간에 따른 보상 및 경험치를 지급합니다."
    )
    @PostMapping("/end")
    public ResponseEntity<ApiResponse<SleepEndResponse>> endSleep(
            @Parameter(hidden = true)
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                sleepService.endSleep(memberId)
        ));
    }

    @Operation(
            summary = "수면 리뷰 작성",
            description = "수면 세션 종료 후 별점과 코멘트를 작성하고, AI 기반 수면 피드백을 생성합니다."
    )
    @PostMapping("/review")
    public ResponseEntity<ApiResponse<SleepReviewResponse>> createReview(
            @RequestBody @Valid SleepReviewRequest request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal Long memberId
    ){
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.CREATED,
                sleepReviewService.createReview(request, memberId)
        ));
    }
}
