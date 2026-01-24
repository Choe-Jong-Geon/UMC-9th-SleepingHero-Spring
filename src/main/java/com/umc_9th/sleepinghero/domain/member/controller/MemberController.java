package com.umc_9th.sleepinghero.domain.member.controller;

import com.umc_9th.sleepinghero.domain.member.dto.req.FriendRequest;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendResponse;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.login_temp.LoginUser;
import com.umc_9th.sleepinghero.domain.member.service.MemberService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import com.umc_9th.sleepinghero.global.enums.Status;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 내부에서만 사용하는 닉네임 추출 헬퍼 메서드
    private String getNickName(Member member) {
        return member.getNickName();
    }

    // 1. 친구 요청 보내기
    @PostMapping("/friends/requests")
    public ApiResponse<String> requestFriends(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId,
            @RequestBody FriendRequest friendRequest) {

        String result = memberService.sendFriendRequest(memberId, friendRequest.getNickName());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @PatchMapping("/friends/requests/{status}")
    public ApiResponse<String> processFriendRequest(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId,
            @PathVariable String status,
            @RequestBody FriendRequest friendRequest) {

        String result = memberService.updateFriendStatus(memberId, friendRequest.getNickName(), status);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("/friends/requests")
    public ApiResponse<List<FriendResponse>> getFriendRequestList(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId) {

        List<FriendResponse> result = memberService.getFriendListByStatus(memberId, Status.PENDING);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("/friends")
    public ApiResponse<List<FriendResponse>> getFriendList(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId) {

        List<FriendResponse> result = memberService.getFriendListByStatus(memberId, Status.ACCEPTED);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @DeleteMapping("/friends")
    public ApiResponse<String> deleteFriend(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId,
            @RequestBody FriendRequest friendRequest) {

        String result = memberService.deleteFriend(memberId, friendRequest.getNickName());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}