package com.umc_9th.sleepinghero.domain.auth.client;

import com.umc_9th.sleepinghero.domain.auth.exception.code.AuthErrorCode;
import com.umc_9th.sleepinghero.domain.auth.model.OauthProfile;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NaverOauthClient extends AbstractOauthClient implements OauthClient {

    private static final String NAVER_ME_URL = "https://openapi.naver.com/v1/nid/me";

    @Override
    public OauthProfile getProfile(String accessToken) {
        Map<?, ?> body = getForMap(NAVER_ME_URL, accessToken);

        Object check = body.get("response");
        if (!(check instanceof Map<?, ?> profile)) {
            throw new GeneralException(AuthErrorCode.OAUTH_PROCESSING_FAILED);
        }

        String providerId = (String) profile.get("id");
        if (providerId == null || providerId.isBlank()) {
            throw new GeneralException(AuthErrorCode.OAUTH_PROCESSING_FAILED);
        }

        String email = (String) profile.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image");

        return new OauthProfile(
                providerId,
                email,
                nickname,
                profileImage
            );
    }

    @Override
    public OauthProvider provider() {
        return OauthProvider.NAVER;
    }
}
