package com.umc_9th.sleepinghero.domain.auth.client;

import com.umc_9th.sleepinghero.domain.auth.model.OauthProfile;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class KakaoOauthClient implements OauthClient {

    private static final String KAKAO_ME_URL = "https://kapi.kakao.com/v2/user/me";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OauthProfile getProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                KAKAO_ME_URL,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<?, ?> body = response.getBody();
        if (body == null) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }

        Map<?, ?> kakaoAccount = (Map<?, ?>) body.get("kakao_account");
        Map<?, ?> profile = (Map<?, ?>) kakaoAccount.get("profile");

        return new OauthProfile(
                String.valueOf(body.get("id")),
                (String) kakaoAccount.get("email"),
                (String) profile.get("nickname"),
                (String) profile.get("profile_image_url")
        );
    }

    @Override
    public OauthProvider provider() {
        return OauthProvider.KAKAO;
    }
}
