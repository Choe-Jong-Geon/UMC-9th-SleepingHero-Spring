package com.umc_9th.sleepinghero.domain.group.controller;

import com.umc_9th.sleepinghero.domain.group.dto.req.*;
import com.umc_9th.sleepinghero.domain.group.service.GroupService;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.login_temp.LoginUser;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ApiResponse<String> createGroup(
            @AuthenticationPrincipal Long memberId,
            @RequestBody GroupMakeRequestDto groupRequest) {

        String result = groupService.createGroup(memberId, groupRequest);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("/rankings")
    public ApiResponse<List<GroupRankResponse>> getGroupRankings() {
        List<GroupRankResponse> result = groupService.getGroupRanking();
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @PostMapping("/invitations")
    public ApiResponse<String> inviteToGroup(
            @AuthenticationPrincipal Long memberId,
            @RequestBody GroupInvitationRequest request) {

        String result = groupService.inviteMember(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @PatchMapping("/requests/{status}") // Post에서 Patch로 수정 (상태 변경이므로)
    public ApiResponse<String> processGroupInvitation(
            @AuthenticationPrincipal Long memberId,
            @PathVariable String status,
            @RequestBody GroupRequestAccept request) {

        String result = groupService.processGroupInvitation(memberId, request.getGroupName(), status);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @PostMapping("/exit") // 경로 중복 제거
    public ApiResponse<String> exitGroup(
            @AuthenticationPrincipal Long memberId,
            @RequestBody GroupExitRequest request) {

        String result = groupService.exitOrKickGroup(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @DeleteMapping("/deletions") // 경로 중복 제거
    public ApiResponse<String> deleteGroup(
            @AuthenticationPrincipal Long memberId,
            @RequestBody GroupDeleteRequest request) {

        String result = groupService.deleteGroup(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

}
