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
public class SleepController {

    private final SleepService sleepService;
    private final SleepReviewService sleepReviewService;

    @Operation(summary = "수면 기록 목록 조회")
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

    @Operation(summary = "수면 기록 단건 조회")
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

    @Operation(summary = "수면 시작")
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

    @Operation(summary = "수면 종료")
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

    @Operation(summary = "수면 리뷰 작성")
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
