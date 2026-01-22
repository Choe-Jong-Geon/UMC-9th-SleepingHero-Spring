package com.umc_9th.sleepinghero.domain.member.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    // 멤버 관련 에러 (400)
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMON400_1", "멤버를 찾을 수 없습니다."),
    INVALID_FRIEND_REQUEST(HttpStatus.BAD_REQUEST, "FRIEND400_1", "잘못된 친구 요청입니다."),
    FRIEND_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "FRIEND400_2", "이미 존재하는 친구 관계입니다."),
    CANNOT_FRIEND_SELF(HttpStatus.BAD_REQUEST, "FRIEND400_3", "자기 자신에게는 친구 요청을 보낼 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
