package com.umc_9th.sleepinghero.domain.hero.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HeroErrorCode implements BaseErrorCode {

    HERO_NOT_FOUND(HttpStatus.NOT_FOUND, "HERO404_1", "존재하지 않는 캐릭터입니다."),
    ALREADY_EXIST_HERO(HttpStatus.BAD_REQUEST, "HERO400_1", "이미 캐릭터를 보유하고 있습니다."),
    LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "HERO404_2", "레벨 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}