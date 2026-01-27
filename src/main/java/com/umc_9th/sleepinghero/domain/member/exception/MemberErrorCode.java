package com.umc_9th.sleepinghero.domain.member.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {


    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "존재하지 않는 사용자입니다."),
    INVALID_MEMBER_STATUS(HttpStatus.BAD_REQUEST, "MEMBER400_1", "유효하지 않은 사용자 상태입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}