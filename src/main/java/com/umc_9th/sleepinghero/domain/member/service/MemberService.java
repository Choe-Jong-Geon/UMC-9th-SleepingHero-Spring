package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponseDTO;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
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

    public String sendFriendRequest(Long memberId, String targetNickName) {
        Member me = findMemberByIdOrThrow(memberId);
        Member friend = findMemberByNickNameOrThrow(targetNickName);

        validateRequest(me, friend);

        friendRepository.save(Friend.builder()
                .member(me)
                .friend(friend)
                .status(Status.PENDING)
                .build());

        return "친구 요청이 완료되었습니다";
    }

    // 2. 수락 및 거절
    public String updateFriendStatus(Long memberId, String senderNickName, String action) {
        Member me = findMemberByIdOrThrow(memberId);
        Member sender = findMemberByNickNameOrThrow(senderNickName);

        // 나(me)에게 온 요청(sender -> me)을 찾음
        Friend request = friendRepository.findByMemberAndFriendAndStatus(sender, me, Status.PENDING)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.INVALID_FRIEND_REQUEST));

        if ("accept".equalsIgnoreCase(action)) {
            request.updateStatus(Status.APPROVE);
            // 양방향 데이터 생성 (나 -> 상대방)
            friendRepository.save(Friend.builder()
                    .member(me)
                    .friend(sender)
                    .status(Status.APPROVE)
                    .build());
            return "친구 요청을 수락하였습니다.";
        }

        friendRepository.delete(request);
        return "친구 요청을 거부하였습니다.";
    }

    // 3. 목록 조회
    @Transactional(readOnly = true)
    public List<FriendResponse> getFriendListByStatus(Long memberId, Status status) {
        Member me = findMemberByIdOrThrow(memberId);

        return friendRepository.findAllByMemberAndStatus(me, status).stream()
                .map(relation -> MemberConverter.toFriendResponse(relation.getFriend()))
                .collect(Collectors.toList());
    }

    // 4. 삭제
    public String deleteFriend(Long memberId, String friendNickName) {
        Member me = findMemberByIdOrThrow(memberId);
        Member friend = findMemberByNickNameOrThrow(friendNickName);

        List<Friend> relations = friendRepository.findAllByMemberAndFriendAndStatus(me, friend, Status.APPROVE);
        List<Friend> reverse = friendRepository.findAllByMemberAndFriendAndStatus(friend, me, Status.APPROVE);

        if (relations.isEmpty() && reverse.isEmpty()) {
            throw new GeneralException(MemberErrorCode.INVALID_FRIEND_REQUEST);
        }

        friendRepository.deleteAll(relations);
        friendRepository.deleteAll(reverse);
        return "친구가 삭제되었습니다.";
    }

    // ------------------------------------ private methode -----------------------------------------

    private Member findMemberByIdOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findMemberByNickNameOrThrow(String nickname) {
        return memberRepository.findByNickName(nickname)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateRequest(Member me, Member friend) {
        if (me.equals(friend)) {
            throw new GeneralException(MemberErrorCode.CANNOT_FRIEND_SELF);
        }
        if (friendRepository.existsByMemberAndFriend(me, friend)) {
            throw new GeneralException(MemberErrorCode.FRIEND_ALREADY_EXISTS);
        }
    }




    public MemberResponseDTO.CheckTutorialDTO checkTutorial(Long memberId) {


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        return MemberResponseDTO.CheckTutorialDTO.builder()
                .finished(member.isTutorialClear())
                .build();
    }



    @Transactional
    public MemberResponseDTO.CompleteTutorialResultDTO completeTutorial(Long memberId, MemberRequestDTO.CompleteTutorialDTO request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.setTutorialClear(request.isFinished());

//        Member savedMember = memberRepository.save(member);

        return MemberResponseDTO.CompleteTutorialResultDTO.builder()
                .memberId(member.getId())
                .finished(member.isTutorialClear())
                .build();
    }
}









