package com.umc_9th.sleepinghero.domain.auth.controller;

import com.umc_9th.sleepinghero.domain.auth.dto.req.OauthLoginRequest;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResponse;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResult;
import com.umc_9th.sleepinghero.domain.auth.dto.res.TokenReissueResponse;
import com.umc_9th.sleepinghero.domain.auth.service.OauthLoginService;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class AuthController {

    private final OauthLoginService oauthLoginService;

    @PostMapping("/naver")
    @Operation(summary = "네이버 로그인", description = """
        프론트엔드에서 전달받은 네이버 OAuth Access Token을 검증한 후,
        회원 정보를 조회하거나 신규 회원을 생성합니다.
        
        이후 우리 서비스 전용 JWT(Access Token, Refresh Token)를 발급합니다.
        """)
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
    @Operation(summary = "카카오 로그인", description = """
        프론트엔드에서 전달받은 카카오 OAuth Access Token을 검증한 후,
        회원 정보를 조회하거나 신규 회원을 생성합니다.
        
        이후 우리 서비스 전용 JWT(Access Token, Refresh Token)를 발급합니다.
        """)
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
    @Operation(
            summary = "Access Token 재발급",
            description = """
        헤더에 포함된 Refresh Token을 검증하여
        새로운 Access Token을 발급합니다.
        """
    )
    public ResponseEntity<ApiResponse<TokenReissueResponse>> reissue(
            @RequestHeader("X-REFRESH-TOKEN") String refreshToken
    ) {
        String newAccessToken = oauthLoginService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(GeneralSuccessCode.OK, new TokenReissueResponse(newAccessToken))
        );
    }
}
