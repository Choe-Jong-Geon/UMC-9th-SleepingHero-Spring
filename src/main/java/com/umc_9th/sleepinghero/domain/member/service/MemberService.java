package com.umc_9th.sleepinghero.domain.member.service;

import ch.qos.logback.core.status.ErrorStatus;
import com.umc_9th.sleepinghero.domain.member.converter.MemberConverter;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendResponse;
import com.umc_9th.sleepinghero.domain.member.entity.Friend;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.FriendRepository;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.global.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    // 공통 로직: 닉네임으로 멤버 조회 (추출)
    private Member findMemberOrThrow(String nickname) {
        return memberRepository.findByNickName(nickname)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    // 1. 친구 요청
    public String sendFriendRequest(String currentMemberNickName, String targetNickName) {
        Member me = findMemberOrThrow(currentMemberNickName);
        Member friend = findMemberOrThrow(targetNickName);

        validateRequest(me, friend);

        friendRepository.save(Friend.builder()
                .member(me)
                .friend(friend)
                .status(Status.PENDING)
                .build());

        return "친구 요청이 완료되었습니다";
    }

    private void validateRequest(Member me, Member friend) {
        if (me.equals(friend)) throw new GeneralException(MemberErrorCode.CANNOT_FRIEND_SELF);
        if (friendRepository.existsByMemberAndFriend(me, friend)) {
            throw new GeneralException(MemberErrorCode.FRIEND_ALREADY_EXISTS);
        }
    }

    // 2. 수락 및 거절
    public String updateFriendStatus(String myNickName, String senderNickName, String action) {
        Member me = findMemberOrThrow(myNickName);
        Member sender = findMemberOrThrow(senderNickName);

        // 나(me)에게 온 요청(sender -> me)을 찾음
        Friend request = friendRepository.findByMemberAndFriendAndStatus(sender, me, Status.PENDING)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.INVALID_FRIEND_REQUEST));

        if ("accept".equalsIgnoreCase(action)) {
            request.updateStatus(Status.ACCEPTED);
            // 양방향 데이터 생성
            friendRepository.save(Friend.builder().member(me).friend(sender).status(Status.ACCEPTED).build());
            return "친구 요청을 수락하였습니다.";
        }

        friendRepository.delete(request);
        return "친구 요청을 거부하였습니다.";
    }

    // 3. 목록 조회 (목록 종류에 따라 Status만 다르게 처리)
    @Transactional(readOnly = true)
    public List<FriendResponse> getFriendListByStatus(String nickname, Status status) {
        Member me = findMemberOrThrow(nickname);
        return friendRepository.findAllByMemberAndStatus(me, status).stream()
                .map(relation -> MemberConverter.toFriendResponse(relation.getFriend()))
                .collect(Collectors.toList());
    }

    // 4. 삭제
    public String deleteFriend(String myNickName, String friendNickName) {
        Member me = findMemberOrThrow(myNickName);
        Member friend = findMemberOrThrow(friendNickName);

        List<Friend> relations = friendRepository.findAllByMemberAndFriendAndStatus(me, friend, Status.ACCEPTED);
        List<Friend> reverse = friendRepository.findAllByMemberAndFriendAndStatus(friend, me, Status.ACCEPTED);

        if (relations.isEmpty() && reverse.isEmpty()) {
            throw new GeneralException(MemberErrorCode.INVALID_FRIEND_REQUEST);
        }

        friendRepository.deleteAll(relations);
        friendRepository.deleteAll(reverse);
        return "친구가 삭제되었습니다.";
    }

}


