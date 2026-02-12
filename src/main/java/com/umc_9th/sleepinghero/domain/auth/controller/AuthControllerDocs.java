package com.umc_9th.sleepinghero.domain.auth.controller;

import com.umc_9th.sleepinghero.domain.auth.dto.req.OauthLoginRequest;
import com.umc_9th.sleepinghero.domain.auth.dto.res.LoginResponse;
import com.umc_9th.sleepinghero.domain.auth.dto.res.TokenReissueResponse;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

public interface AuthControllerDocs {

    @Operation(summary = "네이버 로그인 api", description = """
        프론트엔드에서 전달받은 네이버 OAuth Access Token을 검증한 후,
        회원 정보를 조회하거나 신규 회원을 생성합니다.
        이후 우리 서비스 전용 JWT(Access Token, Refresh Token)를 발급합니다.
        """)
    ResponseEntity<ApiResponse<LoginResponse>> loginNaver(OauthLoginRequest req);

    @Operation(summary = "카카오 로그인", description = """
        프론트엔드에서 전달받은 카카오 OAuth Access Token을 검증한 후,
        회원 정보를 조회하거나 신규 회원을 생성합니다.
        이후 우리 서비스 전용 JWT(Access Token, Refresh Token)를 발급합니다.
        """)
    ResponseEntity<ApiResponse<LoginResponse>> loginKakao(OauthLoginRequest req);

    @Operation(
            summary = "Access Token 재발급 api",
            description = """
        헤더에 포함된 Refresh Token을 검증하여
        새로운 Access Token을 발급합니다.
        Access Token이 만료될 경우를 대비해서 만들었습니다.
        """
    )
    ResponseEntity<ApiResponse<TokenReissueResponse>> reissue(String refreshToken);
}
