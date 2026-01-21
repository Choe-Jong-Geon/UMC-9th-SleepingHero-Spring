package com.umc_9th.sleepinghero.domain.auth.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenReissueResponse {
    private String accessToken;
}
