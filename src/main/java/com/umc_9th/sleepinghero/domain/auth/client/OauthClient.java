package com.umc_9th.sleepinghero.domain.auth.client;

import com.umc_9th.sleepinghero.domain.auth.model.OauthProfile;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;

public interface OauthClient {
    OauthProvider provider();
    OauthProfile getProfile(String accessToken);
}
