package com.umc_9th.sleepinghero.domain.sleep.controller;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepEndResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepReviewResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepStartResponse;
import com.umc_9th.sleepinghero.domain.sleep.service.SleepService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
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


    @GetMapping
    public ResponseEntity<ApiResponse<Page<SleepRecordResponse>>> getSleepRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Long memberId
    ) {

        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                sleepService.getSleepRecords(page,size,memberId
                )));
    }

    @GetMapping("/{sleepRecordId}")
    public ResponseEntity<ApiResponse<SleepRecordResponse>> getSleepRecord(
            @PathVariable Long sleepRecordId,
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK, sleepService.getSleepRecord(sleepRecordId,memberId)
        ));
    }


    @PostMapping("/start")
    public ResponseEntity<ApiResponse<SleepStartResponse>> startSleep(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK, sleepService.startSleep(memberId)
        ));
    }


    @PostMapping("/end")
    public ResponseEntity<ApiResponse<SleepEndResponse>> endSleep(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK,sleepService.endSleep(memberId)
        ));
    }

    @PostMapping("/review")
    public ResponseEntity<ApiResponse<SleepReviewResponse>> createReview(
            @RequestBody @Valid SleepReviewRequest request,
            @AuthenticationPrincipal Long memberId
    ){
        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.CREATED, sleepService.createReview(request, memberId)
        ));
    }

    @PostMapping("/test/record")
    public ResponseEntity<ApiResponse<Long>> testRecord(
            @AuthenticationPrincipal Long memberId
    ){

        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.CREATED, sleepService.testRecord(memberId)
        ));
    }

}
