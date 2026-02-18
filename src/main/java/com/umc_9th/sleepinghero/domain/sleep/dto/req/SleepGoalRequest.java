package com.umc_9th.sleepinghero.domain.sleep.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;


public record SleepGoalRequest(

        @Schema(
                type = "string",
                example = "23:00",
                description = "취침 시간 (HH:mm 형식)"
        )
        @JsonFormat(pattern = "HH:mm")
        LocalTime sleepTime,

        @Schema(
                type = "string",
                example = "07:00",
                description = "기상 시간 (HH:mm 형식)"
        )
        @JsonFormat(pattern = "HH:mm")
        LocalTime wakeTime
) {}
