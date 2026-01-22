package com.umc_9th.sleepinghero.domain.group.controller;

import com.umc_9th.sleepinghero.domain.group.dto.req.GroupMakeRequestDto;
import com.umc_9th.sleepinghero.domain.group.dto.req.GroupRankResponse;
import com.umc_9th.sleepinghero.domain.group.service.GroupService;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.login_temp.LoginUser;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ApiResponse<String> createGroup(
            @LoginUser Member member,
            @RequestBody GroupMakeRequestDto groupRequest) {

        String result = groupService.createGroup(groupRequest, member.getNickName());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);

    }

    @GetMapping("/rankings")
    public ApiResponse<List<GroupRankResponse>> getGroupRankings() {
        try {
            List<GroupRankResponse> result = groupService.getGroupRanking();
            return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
        } catch (Exception e) {
            // 에러 발생 시 지정된 500 에러 코드 반환
            throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
