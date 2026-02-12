package com.umc_9th.sleepinghero.domain.auth.service;
import com.umc_9th.sleepinghero.domain.auth.client.OauthClient;
import com.umc_9th.sleepinghero.domain.auth.converter.AuthConverter;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResult;
import com.umc_9th.sleepinghero.domain.auth.exception.code.AuthErrorCode;
import com.umc_9th.sleepinghero.domain.auth.model.OauthProfile;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.global.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OauthLoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final Map<OauthProvider, OauthClient> clientMap;

    public OauthLoginService(
            MemberRepository memberRepository,
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenService refreshTokenService,
            List<OauthClient> clients
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;

        this.clientMap = clients.stream()
                .collect(Collectors.toMap(OauthClient::provider, Function.identity()));
    }

    @Transactional
    public LoginResult login(OauthProvider provider, String oauthAccessToken) {
        OauthClient client = clientMap.get(provider);
        if (client == null) {
            throw new GeneralException(AuthErrorCode.OAUTH_PROVIDER_NOT_SUPPORTED);
        }
        if (oauthAccessToken == null || oauthAccessToken.isBlank()) {
            throw new GeneralException(AuthErrorCode.OAUTH_ACCESS_TOKEN_REQUIRED);
        }
        OauthProfile profile = client.getProfile(oauthAccessToken);

        Member member = memberRepository
                .findByProviderAndProviderId(provider, profile.providerId())
                .orElseGet(() -> memberRepository.save(AuthConverter.toMember(provider, profile)));

        if (profile.nickname() != null
                && (member.getNickName() == null
                || member.getNickName().equals("유저"))) {

            member.updateNickname(profile.nickname());
        }

        String accessJwt = jwtTokenProvider.createAccessToken(member.getId(), member.getRole());
        String refreshJwt = jwtTokenProvider.createRefreshToken(member.getId());

        refreshTokenService.save(member.getId(), refreshJwt, jwtTokenProvider.refreshTtl());

        return AuthConverter.toLoginResult(member, accessJwt, refreshJwt);
    }

    @Transactional(readOnly = true)
    public String reissueAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new GeneralException(AuthErrorCode.OAUTH_REFRESH_TOKEN_REQUIRED);
        }

        if (!jwtTokenProvider.validate(refreshToken)) {
            throw new GeneralException(AuthErrorCode.JWT_INVALID);
        }

        Long memberId = jwtTokenProvider.getMemberId(refreshToken);

        if (!refreshTokenService.matches(memberId, refreshToken)) {
            throw new GeneralException(AuthErrorCode.JWT_INVALID);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(AuthErrorCode.MEMBER_NOT_FOUND));

        return jwtTokenProvider.createAccessToken(member.getId(), member.getRole());
    }

}
