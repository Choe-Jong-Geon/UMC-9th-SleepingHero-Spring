package com.umc_9th.sleepinghero.domain.auth.dto.req;

import jakarta.validation.constraints.NotBlank;

public record OauthLoginRequest(
        @NotBlank String accessToken
) {
}