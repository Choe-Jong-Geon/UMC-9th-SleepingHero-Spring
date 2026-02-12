package com.umc_9th.sleepinghero.domain.auth.converter;

import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResponse;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResult;
import com.umc_9th.sleepinghero.domain.auth.dto.res.TokenReissueResponse;
import com.umc_9th.sleepinghero.domain.auth.model.OauthProfile;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;

public class AuthConverter {

    public static Member toMember(OauthProvider provider, OauthProfile profile) {
        return Member.builder()
                .provider(provider)
                .providerId(profile.providerId())
                .email(profile.email() == null ? "unknown@oauth" : profile.email())
                .nickName(profile.nickname() == null ? "유저" : profile.nickname())
                .profilePicture(profile.profileImage())
                .build();
    }

    public static LoginResponse toLoginResponse(LoginResult result) {
        return new LoginResponse(result.memberId(), result.nickName(), result.accessToken());
    }

    public static TokenReissueResponse toTokenReissueResponse(String accessToken) {
        return new TokenReissueResponse(accessToken);
    }

    public static LoginResult toLoginResult(Member member, String accessToken, String refreshToken) {
        return new LoginResult(
                member.getId(),
                member.getNickName(),
                accessToken,
                refreshToken
        );
    }
}
