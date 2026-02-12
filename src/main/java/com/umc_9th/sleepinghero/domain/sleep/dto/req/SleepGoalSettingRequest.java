package com.umc_9th.sleepinghero.domain.sleep.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record   SleepGoalSettingRequest(
        @JsonFormat(pattern = "HH:mm") LocalTime sleepTime,
        @JsonFormat(pattern = "HH:mm") LocalTime wakeTime
){
}