package com.umc_9th.sleepinghero.domain.auth.dto.res;

public record LoginResponse(
        Long memberId,
        String nickName,
        String accessToken
) {
}