package com.umc_9th.sleepinghero.domain.auth.client;

import com.umc_9th.sleepinghero.domain.auth.exception.code.AuthErrorCode;
import com.umc_9th.sleepinghero.domain.auth.model.OauthProfile;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoOauthClient extends AbstractOauthClient implements OauthClient {

    private static final String KAKAO_ME_URL = "https://kapi.kakao.com/v2/user/me";

    @Override
    public OauthProfile getProfile(String accessToken) {
        Map<?, ?> body = getForMap(KAKAO_ME_URL, accessToken);

        String providerId = String.valueOf(body.get("id"));
        if (providerId == null || providerId.isBlank() || "null".equals(providerId)) {
            throw new GeneralException(AuthErrorCode.OAUTH_PROCESSING_FAILED);
        }

        Object check = body.get("kakao_account");
        Map<?, ?> kakaoAccount = (check instanceof Map<?, ?> m) ? m : null;

        Object profilecheck = (kakaoAccount == null) ? null : kakaoAccount.get("profile");
        Map<?, ?> profile = (profilecheck instanceof Map<?, ?> m) ? m : null;

        String email = (kakaoAccount == null) ? null : (String) kakaoAccount.get("email");
        String nickname = (profile == null) ? null : (String) profile.get("nickname");
        String profileImage = (profile == null) ? null : (String) profile.get("profile_image_url");

        return new OauthProfile(
                providerId,
                email,
                nickname,
                profileImage
        );
    }

    @Override
    public OauthProvider provider() {
        return OauthProvider.KAKAO;
    }
}
