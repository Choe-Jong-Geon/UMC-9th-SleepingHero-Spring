package com.umc_9th.sleepinghero.domain.auth.model;

public record OauthProfile(
        String providerId,
        String email,
        String nickname,
        String profileImage
) {}
