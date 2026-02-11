package com.umc_9th.sleepinghero.domain.member.controller;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendRankResponse;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponse;
import com.umc_9th.sleepinghero.domain.member.service.MemberService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import com.umc_9th.sleepinghero.domain.member.dto.req.FriendRequest;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendResponse;
import com.umc_9th.sleepinghero.global.enums.Status;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

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
    public ApiResponse<MemberResponse.CheckTutorialDTO> checkTutorial(@AuthenticationPrincipal Long memberId
    ) {
        MemberResponse.CheckTutorialDTO result = memberService.checkTutorial(memberId);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


    @PatchMapping("/users/me/tutorial")
    public ApiResponse<MemberResponse.CompleteTutorialResultDTO> completeTutorial(
            @AuthenticationPrincipal Long memberId,
            @RequestBody MemberRequestDTO.CompleteTutorialDTO request
    ) {
        MemberResponse.CompleteTutorialResultDTO result = memberService.completeTutorial(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("/friends/ranking")
    public ApiResponse<List<FriendRankResponse>> getFriendRankings(
            @Parameter(hidden = true) @AuthenticationPrincipal Long memberId) {

        List<FriendRankResponse> result = memberService.getFriendRanking(memberId);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @DeleteMapping("/users/me")
    public ApiResponse<Void> deleteMe(@AuthenticationPrincipal Long memberId) {
        memberService.deleteMeHard(memberId);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}

