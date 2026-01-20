package com.umc_9th.sleepinghero.domain.sleep.controller;

import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepEndResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepStartResponse;
import com.umc_9th.sleepinghero.domain.sleep.service.SleepService;
import com.umc_9th.sleepinghero.domain.sleep.service.SleepServiceImpl;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/sleep-sessions")
@RequiredArgsConstructor
public class SleepController {

    private final SleepService sleepService;


    @GetMapping
    public ResponseEntity<ApiResponse<Page<SleepRecordResponse>>> getSleepRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                sleepService.getSleepRecords(page,size,1L
                )));
    }

    @GetMapping("/{sleepRecordId}")
    public ResponseEntity<ApiResponse<SleepRecordResponse>> getSleepRecord(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.OK,sleepService.getSleepRecord(1L,1L)
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SleepStartResponse>> startSleep() {

        SleepStartResponse dto = new SleepStartResponse(
                1L,  LocalDateTime.now(), LocalDateTime.now()
        );

        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK,dto));
    }

    @PostMapping("/{sleepRecordId}")
    public ResponseEntity<ApiResponse<SleepEndResponse>> endSleep(@PathVariable Long sleepRecordId) {

        LocalDateTime slept = LocalDateTime.now().minusHours(7);
        LocalDateTime woke = LocalDateTime.now();

        Long durationMinutes = Duration.between(slept, woke).toMinutes();
        int expGained = 10;
        int currentStage = 1;

        SleepEndResponse dto = new SleepEndResponse(
                sleepRecordId,
                slept,
                woke,
                durationMinutes,
                expGained,
                currentStage
        );

        return ResponseEntity.ok(ApiResponse.onSuccess(GeneralSuccessCode.OK,dto));
    }





}
