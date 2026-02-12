package com.umc_9th.sleepinghero.domain.member.validator;

import com.umc_9th.sleepinghero.domain.member.entity.Friend;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.FriendRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberValidator {
    private final FriendRepository friendRepository;

    public void validateFriendRequest(Member me, Member friend) {
        if (me.equals(friend)) {
            throw new GeneralException(MemberErrorCode.CANNOT_FRIEND_SELF);
        }
        if (friendRepository.existsByMemberAndFriend(me, friend)) {
            throw new GeneralException(MemberErrorCode.FRIEND_ALREADY_EXISTS);
        }
    }

    public void validateFriendRelation(List<Friend> relations, List<Friend> reverse) {
        if (relations.isEmpty() && reverse.isEmpty()) {
            throw new GeneralException(MemberErrorCode.INVALID_FRIEND_REQUEST);
        }
    }
}
