package com.umc_9th.sleepinghero.domain.auth.validator;

import com.umc_9th.sleepinghero.domain.auth.client.OauthClient;
import com.umc_9th.sleepinghero.domain.auth.exception.code.AuthErrorCode;
import com.umc_9th.sleepinghero.domain.auth.service.RefreshTokenService;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;

    public void validateOauthAccessToken(String oauthAccessToken) {
        if (oauthAccessToken == null || oauthAccessToken.isBlank()) {
            throw new GeneralException(AuthErrorCode.OAUTH_ACCESS_TOKEN_REQUIRED);
        }
    }

    public OauthClient getClientOrThrow(Map<OauthProvider, OauthClient> clientMap, OauthProvider provider) {
        OauthClient client = clientMap.get(provider);
        if (client == null) {
            throw new GeneralException(AuthErrorCode.OAUTH_PROVIDER_NOT_SUPPORTED);
        }
        return client;
    }

    public void validateRefreshTokenPresent(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new GeneralException(AuthErrorCode.OAUTH_REFRESH_TOKEN_REQUIRED);
        }
    }

    public void validateRefreshTokenJwt(String refreshToken) {
        if (!jwtTokenProvider.validate(refreshToken)) {
            throw new GeneralException(AuthErrorCode.JWT_INVALID);
        }
    }

    public void validateRefreshTokenMatches(Long memberId, String refreshToken) {
        if (!refreshTokenService.matches(memberId, refreshToken)) {
            throw new GeneralException(AuthErrorCode.JWT_INVALID);
        }
    }
}
