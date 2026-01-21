package com.umc_9th.sleepinghero.domain.auth.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long memberId;
    private String nickName;
    private String accessToken;
}
