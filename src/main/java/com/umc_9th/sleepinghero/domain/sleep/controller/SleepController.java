package com.umc_9th.sleepinghero.domain.sleep.controller;

import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepGoalSettingRequest;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.*;
import com.umc_9th.sleepinghero.domain.sleep.service.SleepReviewService;
import com.umc_9th.sleepinghero.domain.sleep.service.SleepService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Parameter;
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
public class SleepController implements SleepControllerDocs {

    private final SleepService sleepService;
    private final SleepReviewService sleepReviewService;


    @Override
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


    @Override
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


    @PutMapping("/goal")
    public ResponseEntity<ApiResponse<SleepGoalSettingResponse>> settingSleepGoal(
            @RequestBody SleepGoalSettingRequest request,
            @AuthenticationPrincipal Long memberId
    ){
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.CREATED,sleepService.settingSleep(request, memberId)
        ));
    }
}
