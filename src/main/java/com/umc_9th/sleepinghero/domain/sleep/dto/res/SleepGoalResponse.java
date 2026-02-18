package com.umc_9th.sleepinghero.domain.sleep.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record SleepGoalResponse(

        @Schema(type = "string", example = "23:00", description = "설정된 취침 시간")
        @JsonFormat(pattern = "HH:mm")
        LocalTime sleepTime,

        @Schema(type = "string", example = "07:00", description = "설정된 기상 시간")
        @JsonFormat(pattern = "HH:mm")
        LocalTime wakeTime,

        @Schema(example = "480", description = "총 수면 목표 시간(분 단위)")
        long totalMinutes
) {}
