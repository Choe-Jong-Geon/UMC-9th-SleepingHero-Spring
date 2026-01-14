package com.umc_9th.sleepinghero.domain.sleep.controller;

import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepEndResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepStartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/sleep-sessions")
@RequiredArgsConstructor
public class SleepController {


    @GetMapping
    public ResponseEntity<Page<SleepRecordResponse>> getSleepRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(Page.empty());
    }

    @GetMapping("/{sleepRecordId}")
    public ResponseEntity<SleepRecordResponse> getSleepRecord(@PathVariable Long sleepRecordId) {
        return ResponseEntity.ok(new SleepRecordResponse(
                sleepRecordId, LocalDateTime.now(),LocalDateTime.now(), true
        ));
    }

    @PostMapping
    public ResponseEntity<SleepStartResponse> startSleep() {
        return ResponseEntity.ok(new SleepStartResponse(1L, LocalTime.now(),LocalTime.now()));
    }

    @PostMapping("/{sleepRecordId}")
    public ResponseEntity<SleepEndResponse> endSleep(@PathVariable Long sleepRecordId) {
        return ResponseEntity.ok(new SleepEndResponse(
                sleepRecordId, LocalDateTime.now(),LocalDateTime.now()
        ));
    }





}
