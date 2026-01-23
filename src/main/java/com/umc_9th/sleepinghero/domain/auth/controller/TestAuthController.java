package com.umc_9th.sleepinghero.domain.auth.controller;

import com.umc_9th.sleepinghero.domain.member.enums.Role;
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

    @PostMapping
    public String issue(@RequestParam Long memberId) {
        return jwtTokenProvider.createAccessToken(memberId, Role.ROLE_USER);
    }
}