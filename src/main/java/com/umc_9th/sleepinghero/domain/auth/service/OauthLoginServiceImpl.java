package com.umc_9th.sleepinghero.domain.auth.service;
import com.umc_9th.sleepinghero.domain.auth.client.OauthClient;
import com.umc_9th.sleepinghero.domain.auth.converter.AuthConverter;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResult;
import com.umc_9th.sleepinghero.domain.auth.exception.code.AuthErrorCode;
import com.umc_9th.sleepinghero.domain.auth.model.OauthProfile;
import com.umc_9th.sleepinghero.domain.auth.validator.AuthValidator;
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
public class OauthLoginServiceImpl implements OauthLoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final Map<OauthProvider, OauthClient> clientMap;
    private final AuthValidator authValidator;

    public OauthLoginServiceImpl(
            MemberRepository memberRepository,
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenServiceImpl refreshTokenServiceImpl,
            List<OauthClient> clients,
            AuthValidator authValidator
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenServiceImpl = refreshTokenServiceImpl;
        this.authValidator = authValidator;

        this.clientMap = clients.stream()
                .collect(Collectors.toMap(OauthClient::provider, Function.identity()));
    }

    @Transactional
    public LoginResult login(OauthProvider provider, String oauthAccessToken) {

        OauthClient client = authValidator.getClientOrThrow(clientMap, provider);

        authValidator.validateOauthAccessToken(oauthAccessToken);

        OauthProfile profile = client.getProfile(oauthAccessToken);

        Member member = memberRepository
                .findByProviderAndProviderId(provider, profile.providerId())
                .orElseGet(() -> memberRepository.save(AuthConverter.toMember(provider, profile)));

        updateNicknameIfNeeded(member, profile);

        String accessJwt = jwtTokenProvider.createAccessToken(member.getId(), member.getRole());
        String refreshJwt = jwtTokenProvider.createRefreshToken(member.getId());

        refreshTokenServiceImpl.save(member.getId(), refreshJwt, jwtTokenProvider.refreshTtl());

        return AuthConverter.toLoginResult(member, accessJwt, refreshJwt);
    }

    @Transactional(readOnly = true)
    public String reissueAccessToken(String refreshToken) {
        authValidator.validateRefreshTokenPresent(refreshToken);
        authValidator.validateRefreshTokenJwt(refreshToken);

        Long memberId = jwtTokenProvider.getMemberId(refreshToken);

        authValidator.validateRefreshTokenMatches(memberId, refreshToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(AuthErrorCode.MEMBER_NOT_FOUND));

        return jwtTokenProvider.createAccessToken(member.getId(), member.getRole());
    }

    private void updateNicknameIfNeeded(Member member, OauthProfile profile) {
        if (profile.nickname() == null || profile.nickname().isBlank()) return;

        if (member.getNickName() == null || member.getNickName().equals("유저")) {
            member.updateNickname(profile.nickname());
        }
    }
}
