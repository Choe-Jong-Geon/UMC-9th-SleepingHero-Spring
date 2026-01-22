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
            @Parameter(hidden = true) @LoginUser Member member,
            @RequestBody FriendRequest friendRequest) {

        String result = memberService.sendFriendRequest(getNickName(member), friendRequest.getNickName());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    // 2. 친구 요청 수락/거절
    @PatchMapping("/friends/requests/{status}")
    public ApiResponse<String> processFriendRequest(
            @Parameter(hidden = true) @LoginUser Member member,
            @PathVariable String status,
            @RequestBody FriendRequest friendRequest) {

        String result = memberService.updateFriendStatus(getNickName(member), friendRequest.getNickName(), status);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    // 3. 받은 친구 요청 목록 조회 (PENDING 상태)
    @GetMapping("/friends/requests")
    public ApiResponse<List<FriendResponse>> getFriendRequestList(
            @Parameter(hidden = true) @LoginUser Member member) {

        // 나에게 요청을 보낸 사람들이 '대기' 중인 목록을 가져옵니다.
        List<FriendResponse> result = memberService.getFriendListByStatus(getNickName(member), Status.PENDING);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    // 4. 내 친구 목록 조회 (ACCEPTED 상태)
    @GetMapping("/friends")
    public ApiResponse<List<FriendResponse>> getFriendList(
            @Parameter(hidden = true) @LoginUser Member member) {

        // 서로 수락되어 '친구'인 목록을 가져옵니다.
        List<FriendResponse> result = memberService.getFriendListByStatus(getNickName(member), Status.ACCEPTED);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    // 5. 친구 삭제
    @DeleteMapping("/friends") // 경로 명시
    public ApiResponse<String> deleteFriend(
            @Parameter(hidden = true) @LoginUser Member member,
            @RequestBody FriendRequest friendRequest) {

        String result = memberService.deleteFriend(getNickName(member), friendRequest.getNickName());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}