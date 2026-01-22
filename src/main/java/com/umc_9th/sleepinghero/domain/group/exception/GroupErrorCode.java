package com.umc_9th.sleepinghero.domain.group.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupErrorCode implements BaseErrorCode {

    GROUP_NOT_MADE(HttpStatus.BAD_REQUEST, "COMMON400_1", "그룹을 만들 수 없습니다. 그룹 이름, 설명을 입력해주세요");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
