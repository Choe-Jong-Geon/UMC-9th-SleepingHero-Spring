package com.umc_9th.sleepinghero.domain.member.converter;

import com.umc_9th.sleepinghero.domain.member.dto.res.FriendResponse;
import com.umc_9th.sleepinghero.domain.member.entity.Member;

public class MemberConverter {

    public static FriendResponse toFriendResponse(Member friend) {
        return FriendResponse.builder()
                .memberId(friend.getId())
                .nickname(friend.getNickName())
                .profilePicture(friend.getProfilePicture())
                .build();
    }
}
