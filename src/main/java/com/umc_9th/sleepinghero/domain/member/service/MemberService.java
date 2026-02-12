package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendRankResponse;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendResponse;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponse;
import com.umc_9th.sleepinghero.global.enums.Status;

import java.util.List;

public interface MemberService {

    String sendFriendRequest(Long memberId, String targetNickName);

    String updateFriendStatus(Long memberId, String senderNickName, String action);

    List<FriendResponse> getFriendListByStatus(Long memberId, Status status);

    String deleteFriend(Long memberId, String friendNickName);

    List<FriendRankResponse> getFriendRanking(Long memberId);

    void deleteMeHard(Long memberId);

    MemberResponse.CheckTutorialDTO checkTutorial(Long memberId);

    MemberResponse.CompleteTutorialResultDTO completeTutorial(Long memberId, MemberRequestDTO.CompleteTutorialDTO request);


}
