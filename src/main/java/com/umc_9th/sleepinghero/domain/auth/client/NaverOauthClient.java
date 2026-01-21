package com.umc_9th.sleepinghero.domain.auth.client;

import com.umc_9th.sleepinghero.domain.auth.model.OauthProfile;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NaverOauthClient implements OauthClient {

    private static final String NAVER_ME_URL = "https://openapi.naver.com/v1/nid/me";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OauthProfile getProfile(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    NAVER_ME_URL,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<?, ?> body = response.getBody();
            if (body == null || body.get("response") == null) {
                throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
            }

            Map<?, ?> r = (Map<?, ?>) body.get("response");

            return new OauthProfile(
                    (String) r.get("id"),
                    (String) r.get("email"),
                    (String) r.get("nickname"),
                    (String) r.get("profile_image")
            );
        } catch (RestClientException e) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        } catch (ClassCastException e) {
            throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public OauthProvider provider() {
        return OauthProvider.NAVER;
    }
}
