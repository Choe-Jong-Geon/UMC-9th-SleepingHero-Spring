package com.umc_9th.sleepinghero.domain.group.controller;

import com.umc_9th.sleepinghero.domain.group.dto.req.*;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInsideRankingResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupRankResponse;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "group-controller", description = "그룹 생성, 초대, 랭킹 및 멤버 관리 API")
public interface GroupControllerDocs {

    @Operation(summary = "그룹 생성 API", description = "새로운 히어로 그룹을 생성합니다.")
    ApiResponse<String> createGroup(Long memberId, GroupMakeRequestDto groupRequest);

    @Operation(summary = "전체 그룹 랭킹 조회 API", description = "모든 그룹의 평균 수면 시간 및 인원수를 기준으로 랭킹을 조회합니다.")
    ApiResponse<List<GroupRankResponse>> getGroupRankings();

    @Operation(summary = "그룹 초대/가입 요청 API", description = "특정 유저를 그룹에 초대하거나 가입 요청을 보냅니다.")
    ApiResponse<String> inviteToGroup(Long memberId, GroupInvitationRequest request);

    @Operation(summary = "그룹 초대 승인/거절 API", description = "받은 그룹 초대 요청을 받은 멤버 수락 Or 거절 (APPROVE/REJECTED")
    ApiResponse<String> processGroupInvitation(Long memberId, String status, GroupRequestAccept request);

    @Operation(summary = "그룹 탈퇴 및 추방 API", description = "그룹을 탈퇴하거나, 마스터 권한으로 멤버를 추방합니다.")
    ApiResponse<String> exitGroup(Long memberId, GroupExitRequest request);

    @Operation(summary = "그룹 삭제 API", description = "그룹 마스터가 그룹을 완전히 삭제합니다. (혼자 남았을 때만 가능)")
    ApiResponse<String> deleteGroup(Long memberId, GroupDeleteRequest request);

    @Operation(summary = "그룹 내부 멤버 랭킹 조회 API", description = "특정 그룹 내 멤버들의 수면 시간, 연속 일수, 레벨을 기준으로 랭킹을 조회합니다.")
    ApiResponse<GroupInsideRankingResponse> getGroupInsideRanking(String groupName);
}