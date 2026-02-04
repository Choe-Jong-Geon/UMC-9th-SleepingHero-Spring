package com.umc_9th.sleepinghero.domain.member.converter;

import com.umc_9th.sleepinghero.domain.member.dto.res.FriendRankResponse;
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

    public static FriendRankResponse toFriendRankResponse(Member member, Long totalSeconds, int rank) {
        long h = totalSeconds / 3600;
        long m = (totalSeconds % 3600) / 60;
        long s = totalSeconds % 60;

        return FriendRankResponse.builder()
                .nickName(member.getNickName())
                .totalSleepTime(String.format("%02d:%02d:%02d", h, m, s))
                .rank(rank)
                .build();
    }
}
