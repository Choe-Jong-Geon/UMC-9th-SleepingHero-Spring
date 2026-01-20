package com.umc_9th.sleepinghero.domain.sleep.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SleepErrorCode implements BaseErrorCode {

    SLEEP_RECORD_BAD_REQUEST(HttpStatus.BAD_REQUEST,
            "COMMON400_1",
            "이미 기상 상태입니다."),

    SLEEP_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND,
            "COMMON404_1",
            "해당 수면 기록을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
