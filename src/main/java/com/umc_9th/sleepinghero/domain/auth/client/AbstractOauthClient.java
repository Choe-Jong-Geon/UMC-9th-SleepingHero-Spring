package com.umc_9th.sleepinghero.domain.auth.client;

import com.umc_9th.sleepinghero.domain.auth.exception.code.AuthErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class AbstractOauthClient {

    protected final RestTemplate restTemplate = new RestTemplate();

    protected Map<?, ?> getForMap(String url, String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class
            );

            Map<?, ?> body = response.getBody();
            if (body == null) {
                throw new GeneralException(AuthErrorCode.OAUTH_PROCESSING_FAILED);
            }
            return body;

        } catch (HttpClientErrorException e) {
            throw new GeneralException(AuthErrorCode.OAUTH_TOKEN_INVALID);
        } catch (RestClientException e) {
            throw new GeneralException(AuthErrorCode.OAUTH_PROCESSING_FAILED);
        }
    }
}
