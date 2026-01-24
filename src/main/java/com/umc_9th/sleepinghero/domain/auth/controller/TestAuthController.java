package com.umc_9th.sleepinghero.domain.auth.controller;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.domain.member.enums.Role;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/token")
@RequiredArgsConstructor
public class TestAuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping
    public String issue(@RequestParam Long memberId) {
        memberRepository.findById(memberId)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .provider(OauthProvider.NAVER)
                                .providerId("testUserProviderId-" + memberId)
                                .email("test-"+memberId +"@test.com")
                                .nickName("테스트유저")
                                .build()
                ));
        return jwtTokenProvider.createAccessToken(memberId, Role.ROLE_USER);
    }
}