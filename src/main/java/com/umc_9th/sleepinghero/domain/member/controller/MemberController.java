package com.umc_9th.sleepinghero.domain.member.controller;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponseDTO;
import com.umc_9th.sleepinghero.domain.member.service.MemberService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
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

        List<FriendResponse> result = memberService.getFriendListByStatus(memberId, Status.APPROVE);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @DeleteMapping("/friends")
    public ApiResponse<String> deleteFriend(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId,
            @RequestBody FriendRequest friendRequest) {

        String result = memberService.deleteFriend(memberId, friendRequest.getNickName());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


    @GetMapping("/users/me/tutorial")
    @Operation(summary = "튜토리얼 조회 API", description = "유저의 튜토리얼 완료 여부를 조회합니다.")
    public ApiResponse<MemberResponseDTO.CheckTutorialDTO> checkTutorial(@AuthenticationPrincipal Long memberId
    ) {
        MemberResponseDTO.CheckTutorialDTO result = memberService.checkTutorial(memberId);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


    @PatchMapping("/users/me/tutorial")
    @Operation(summary = "튜토리얼 완료 처리 API", description = "유저의 튜토리얼 상태를 변경합니다.")
    public ApiResponse<MemberResponseDTO.CompleteTutorialResultDTO> completeTutorial(
            @AuthenticationPrincipal Long memberId,
            @RequestBody MemberRequestDTO.CompleteTutorialDTO request
    ) {
        MemberResponseDTO.CompleteTutorialResultDTO result = memberService.completeTutorial(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

}

