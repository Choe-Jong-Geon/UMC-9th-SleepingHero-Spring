package com.umc_9th.sleepinghero.domain.test.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TestErrorCode implements BaseErrorCode {

    GENERAL_EXCEPTION(HttpStatus.BAD_REQUEST, "TEST400_1", "이거는 테스트");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
