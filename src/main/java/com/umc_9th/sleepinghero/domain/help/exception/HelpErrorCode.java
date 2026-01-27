package com.umc_9th.sleepinghero.domain.help.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HelpErrorCode implements BaseErrorCode {

    INQUIRY_URL_NOT_CONFIGURED(HttpStatus.INTERNAL_SERVER_ERROR, "HELP500_1", "문의하기 링크가 설정되지 않았습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}