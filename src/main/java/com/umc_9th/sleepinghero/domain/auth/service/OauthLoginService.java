package com.umc_9th.sleepinghero.domain.auth.service;

import com.umc_9th.sleepinghero.domain.auth.client.OauthClient;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResult;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.global.jwt.JwtTokenProvider;

import java.util.List;

public interface OauthLoginService {

    LoginResult login(OauthProvider provider, String oauthAccessToken);

    String reissueAccessToken(String refreshToken);


}
