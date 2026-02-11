package com.umc_9th.sleepinghero.domain.member.controller;

import com.umc_9th.sleepinghero.domain.member.dto.req.FriendRequest;
import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendRankResponse;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendResponse;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponse;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Member 관련 API", description = "친구 관리, 튜토리얼, 랭킹 및 계정 관련 API")
public interface MemberControllerDocs {

    @Operation(summary = "친구 요청 API", description = "닉네임을 통해 상대방에게 친구 요청을 보냅니다.")
    ApiResponse<String> requestFriends(Long memberId, FriendRequest friendRequest);

    @Operation(summary = "친구 요청 수락/거절 API", description = "받은 친구 요청의 상태를 변경합니다 (APPROVE/REJECTED).")
    ApiResponse<String> processFriendRequest(Long memberId, String status, FriendRequest friendRequest);

    @Operation(summary = "대기 중인 친구 요청 목록 조회 API", description = "나에게 온 친구 요청 중 대기(PENDING) 상태인 목록을 조회합니다.")
    ApiResponse<List<FriendResponse>> getFriendRequestList(Long memberId);

    @Operation(summary = "친구 목록 조회 API", description = "서로 수락(APPROVE)된 친구 목록을 조회합니다.")
    ApiResponse<List<FriendResponse>> getFriendList(Long memberId);

    @Operation(summary = "친구 삭제 API", description = "기존의 친구 관계를 삭제합니다.")
    ApiResponse<String> deleteFriend(Long memberId, FriendRequest friendRequest);

    @Operation(summary = "튜토리얼 조회 API", description = "유저의 튜토리얼 완료 여부를 조회합니다.")
    ApiResponse<MemberResponse.CheckTutorialDTO> checkTutorial(Long memberId);

    @Operation(summary = "튜토리얼 완료 처리 API", description = "유저의 튜토리얼 상태를 완료로 변경합니다.")
    ApiResponse<MemberResponse.CompleteTutorialResultDTO> completeTutorial(Long memberId, MemberRequestDTO.CompleteTutorialDTO request);

    @Operation(summary = "친구 수면 랭킹 조회 API", description = "나와 친구들의 수면 시간을 기준으로 랭킹을 조회합니다.")
    ApiResponse<List<FriendRankResponse>> getFriendRankings(Long memberId);

    @Operation(summary = "회원 탈퇴 API", description = "현재 로그인한 유저의 정보를 영구 삭제합니다.")
    ApiResponse<Void> deleteMe(Long memberId);
}