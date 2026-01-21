package com.umc_9th.sleepinghero.domain.auth.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 내부용 (내부에서 refreshToken 필요)
@Getter
@AllArgsConstructor
public class LoginResult {
    private Long memberId;
    private String nickName;
    private String accessToken;
    private String refreshToken;
}

