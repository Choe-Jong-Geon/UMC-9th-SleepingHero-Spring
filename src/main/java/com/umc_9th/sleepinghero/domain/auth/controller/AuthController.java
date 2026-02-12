package com.umc_9th.sleepinghero.domain.auth.controller;

import com.umc_9th.sleepinghero.domain.auth.dto.req.OauthLoginRequest;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResponse;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResult;
import com.umc_9th.sleepinghero.domain.auth.dto.res.TokenReissueResponse;
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
public class AuthController implements  AuthControllerDocs {

    private final OauthLoginService oauthLoginService;

    @PostMapping("/naver")
    public ResponseEntity<ApiResponse<LoginResponse>> loginNaver(@RequestBody OauthLoginRequest req) {
        LoginResult result = oauthLoginService.login(OauthProvider.NAVER, req.accessToken());

        return ResponseEntity.ok()
                .header("X-REFRESH-TOKEN", result.refreshToken())
                .body(ApiResponse.onSuccess(
                        GeneralSuccessCode.OK,
                        new LoginResponse(result.memberId(),
                                result.nickName(),
                                result.accessToken())
                ));
    }

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> loginKakao(@RequestBody OauthLoginRequest req) {
        LoginResult result = oauthLoginService.login(OauthProvider.KAKAO, req.accessToken());

        return ResponseEntity.ok()
                .header("X-REFRESH-TOKEN", result.refreshToken())
                .body(ApiResponse.onSuccess(
                        GeneralSuccessCode.OK,
                        new LoginResponse(result.memberId(),
                                result.nickName(),
                                result.accessToken())
                ));
    }
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenReissueResponse>> reissue(
            @RequestHeader("X-REFRESH-TOKEN") String refreshToken
    ) {
        String newAccessToken = oauthLoginService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(GeneralSuccessCode.OK, new TokenReissueResponse(newAccessToken))
        );
    }
}
