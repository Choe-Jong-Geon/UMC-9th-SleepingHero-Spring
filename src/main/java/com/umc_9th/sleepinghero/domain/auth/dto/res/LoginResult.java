package com.umc_9th.sleepinghero.domain.auth.dto.res;

// 내부용 (내부에서 refreshToken 필요)
public record LoginResult(
        Long memberId,
        String nickName,
        String accessToken,
        String refreshToken
) {
}

