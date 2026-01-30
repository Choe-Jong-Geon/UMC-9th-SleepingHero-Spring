package com.umc_9th.sleepinghero.domain.sleep.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SleepErrorCode implements BaseErrorCode {


    SLEEP_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST,
            "SLEEP400_1",
            "현재 수면 중인 상태가 아닙니다."),
    SLEEP_GOAL_INVALID(HttpStatus.BAD_REQUEST,
            "SLEEP400_2",
            "요청한 수면 시간이 목표 수면 시간과 다릅니다."),
    SLEEP_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND,
            "SLEEP404_1",
            "해당 수면 기록을 찾을 수 없습니다."),
    SLEEP_ALREADY_IN_PROGRESS(HttpStatus.CONFLICT,
            "SLEEP_409_1",
                    "해당 사용자는 이미 수면 중 입니다."),

    SLEEP_GOAL_NOT_FOUND(HttpStatus.NOT_FOUND,
            "SLEEP404_2",
            "해당 사용자의 수면 목표가 설정되어 있지 않습니다."),



    SLEEP_SESSION_INCONSISTENT(HttpStatus.INTERNAL_SERVER_ERROR,
            "SLEEP500_1",
            "수면 상태와 수면 기록이 일치하지 않습니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;
}
