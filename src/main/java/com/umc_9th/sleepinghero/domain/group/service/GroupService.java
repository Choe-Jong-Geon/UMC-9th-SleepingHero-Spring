package com.umc_9th.sleepinghero.domain.group.service;

import com.umc_9th.sleepinghero.domain.group.dto.req.GroupDeleteRequest;
import com.umc_9th.sleepinghero.domain.group.dto.req.GroupExitRequest;
import com.umc_9th.sleepinghero.domain.group.dto.req.GroupInvitationRequest;
import com.umc_9th.sleepinghero.domain.group.dto.req.GroupMakeRequestDto;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInsideRankingResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupInvitationResponse;
import com.umc_9th.sleepinghero.domain.group.dto.res.GroupRankResponse;

import java.util.List;

public interface GroupService {

    String createGroup(Long memberId, GroupMakeRequestDto request);

    List<GroupRankResponse> getGroupRanking(Long memberId);

    GroupInsideRankingResponse getGroupInsideRanking(String groupName);

    String inviteMember(Long memberId, GroupInvitationRequest request);

    List<GroupInvitationResponse> getPendingGroupRequests(Long memberId);

    String processGroupInvitation(Long memberId, String groupName, String status);

    String exitOrKickGroup(Long memberId, GroupExitRequest request);

    String deleteGroup(Long memberId, GroupDeleteRequest request);

}
