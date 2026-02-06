package com.umc_9th.sleepinghero.domain.test.controller;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.domain.member.enums.Role;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.sleep.service.SleepService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import com.umc_9th.sleepinghero.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestContoller {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final SleepService sleepService;

    @GetMapping("/health-check")
    @Operation(summary = "서버 연결 체크")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("server is running");
    }

    @PostMapping("/test/token")
    @Operation(summary = "테스트 유저 생성", description = "이 api 호출 시 토큰 자동 생성")
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

    @PostMapping("/test/record")
    @Operation(
            summary = "테스트용 목표 수면시간 및 수면 기록 자동 생성 api",
            description = "AI 피드백 생성을 위한 목표 설정 및 수면 기록 자동 생성"
    )
    public ResponseEntity<ApiResponse<Long>> testRecord(
            @AuthenticationPrincipal Long memberId
    ){

        return ResponseEntity.ok(ApiResponse.onSuccess(
                GeneralSuccessCode.CREATED, sleepService.testRecord(memberId)
        ));
    }

}
