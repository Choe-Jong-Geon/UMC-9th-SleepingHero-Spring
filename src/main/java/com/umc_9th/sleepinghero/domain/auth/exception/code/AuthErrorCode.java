package com.umc_9th.sleepinghero.domain.auth.exception.code;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    // 400
    OAUTH_ACCESS_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST,
            "AUTH400_1", "OAuth accessToken이 필요합니다."),
    OAUTH_PROVIDER_NOT_SUPPORTED(HttpStatus.BAD_REQUEST,
            "AUTH400_2", "지원하지 않는 로그인 제공자입니다."),
    OAUTH_REFRESH_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST,
            "AUTH400_3", "RefreshToken이 필요합니다."),

    // 토큰 관련 에러 (401)
    OAUTH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,
            "AUTH401_1", "유효하지 않은 OAuth 토큰입니다."),
    OAUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,
            "AUTH401_2", "OAuth 토큰이 만료되었습니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED,
            "AUTH401_3", "유효하지 않은 토큰입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,
            "AUTH401_4", "토큰이 만료되었습니다."),

    // 404
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,
            "MEMBER404_1", "존재하지 않는 사용자입니다."),
    // 500
    OAUTH_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,
            "AUTH500_1", "소셜 로그인 처리 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
