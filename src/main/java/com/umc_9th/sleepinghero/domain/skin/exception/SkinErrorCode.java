package com.umc_9th.sleepinghero.domain.skin.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SkinErrorCode implements BaseErrorCode {

    SKIN_NOT_FOUND(HttpStatus.NOT_FOUND, "SKIN404_1", "존재하지 않는 스킨입니다."),
    SKIN_NOT_OWNED(HttpStatus.FORBIDDEN, "SKIN403_1", "보유하지 않은 스킨은 착용할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}