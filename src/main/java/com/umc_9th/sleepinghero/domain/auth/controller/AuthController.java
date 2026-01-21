package com.umc_9th.sleepinghero.domain.auth.controller;

import com.umc_9th.sleepinghero.domain.auth.dto.req.OauthLoginRequest;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResponse;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResult;
import com.umc_9th.sleepinghero.domain.auth.service.OauthLoginService;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class AuthController {

    private final OauthLoginService oauthLoginService;

    @PostMapping("/naver")
    public ResponseEntity<ApiResponse<LoginResponse>> loginNaver(@RequestBody OauthLoginRequest req) {
        LoginResult result = oauthLoginService.login(OauthProvider.NAVER, req.getAccessToken());

        return ResponseEntity.ok()
                .header("X-REFRESH-TOKEN", result.getRefreshToken())
                .body(ApiResponse.onSuccess(
                        GeneralSuccessCode.OK,
                        new LoginResponse(result.getMemberId(), result.getNickName(), result.getAccessToken())
                ));
    }

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> loginKakao(@RequestBody OauthLoginRequest req) {
        LoginResult result = oauthLoginService.login(OauthProvider.KAKAO, req.getAccessToken());

        return ResponseEntity.ok()
                .header("X-REFRESH-TOKEN", result.getRefreshToken())
                .body(ApiResponse.onSuccess(
                        GeneralSuccessCode.OK,
                        new LoginResponse(result.getMemberId(), result.getNickName(), result.getAccessToken())
                ));
    }
}
