package com.umc_9th.sleepinghero.domain.group.controller;

import com.umc_9th.sleepinghero.domain.group.dto.req.*;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInsideRankingResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInvitaionRequest;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupRankResponse;
import com.umc_9th.sleepinghero.domain.group.service.GroupService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController implements GroupControllerDocs {

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

    @GetMapping("/requests/pending")
    public ApiResponse<List<GroupInvitaionRequest>> getPendingRequests(
            @AuthenticationPrincipal Long memberId) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, groupService.getPendingGroupRequests(memberId));
    }

    @PatchMapping("/requests/{status}")
    public ApiResponse<String> processGroupInvitation(
            @AuthenticationPrincipal Long memberId,
            @PathVariable String status,
            @RequestBody GroupRequestAccept request) {

        String result = groupService.processGroupInvitation(memberId, request.getGroupName(), status);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @PostMapping("/exit")
    public ApiResponse<String> exitGroup(
            @AuthenticationPrincipal Long memberId,
            @RequestBody GroupExitRequest request) {

        String result = groupService.exitOrKickGroup(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @DeleteMapping("/deletions")
    public ApiResponse<String> deleteGroup(
            @AuthenticationPrincipal Long memberId,
            @RequestBody GroupDeleteRequest request) {

        String result = groupService.deleteGroup(memberId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @GetMapping("/ranking/inside")
    public ApiResponse<GroupInsideRankingResponse> getGroupInsideRanking(
            @RequestParam String groupName) {

        GroupInsideRankingResponse result = groupService.getGroupInsideRanking(groupName);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

}
