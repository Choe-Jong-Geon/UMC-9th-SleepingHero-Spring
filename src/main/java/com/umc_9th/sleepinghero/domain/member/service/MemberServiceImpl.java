package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendRankResponse;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponse;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.member.validator.MemberValidator;
import com.umc_9th.sleepinghero.domain.sleep.calculator.SleepCalculator;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.domain.member.converter.MemberConverter;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendResponse;
import com.umc_9th.sleepinghero.domain.member.entity.Friend;
import com.umc_9th.sleepinghero.domain.member.repository.FriendRepository;
import com.umc_9th.sleepinghero.global.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final MemberValidator memberValidator;
    private final SleepCalculator sleepCalculator;
    private final MemberCleanupManager memberCleanupManager;

    public String sendFriendRequest(Long memberId, String targetNickName) {
        Member me = findMemberByIdOrThrow(memberId);
        Member friend = findMemberByNickNameOrThrow(targetNickName);
        memberValidator.validateFriendRequest(me, friend);

        friendRepository.save(Friend.builder().member(me).friend(friend).status(Status.PENDING).build());
        return "친구 요청이 완료되었습니다";
    }

    public String updateFriendStatus(Long memberId, String senderNickName, String action) {
        Member me = findMemberByIdOrThrow(memberId);
        Member sender = findMemberByNickNameOrThrow(senderNickName);
        Friend request = friendRepository.findByMemberAndFriendAndStatus(sender, me, Status.PENDING)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.INVALID_FRIEND_REQUEST));

        if ("APPROVE".equals(action)) {
            request.updateStatus(Status.APPROVE);
            friendRepository.save(Friend.builder().member(me).friend(sender).status(Status.APPROVE).build());
            return "친구 요청을 수락하였습니다.";
        }
        friendRepository.delete(request);
        return "친구 요청을 거부하였습니다.";
    }

    @Transactional(readOnly = true)
    public List<FriendResponse> getFriendListByStatus(Long memberId, Status status) {
        Member me = findMemberByIdOrThrow(memberId);
        return friendRepository.findAllByFriendAndStatus(me, status).stream()
                .map(relation -> MemberConverter.toFriendResponse(relation.getMember()))
                .collect(Collectors.toList());
    }

    public String deleteFriend(Long memberId, String friendNickName) {
        Member me = findMemberByIdOrThrow(memberId);
        Member friend = findMemberByNickNameOrThrow(friendNickName);
        List<Friend> relations = friendRepository.findAllByMemberAndFriendAndStatus(me, friend, Status.APPROVE);
        List<Friend> reverse = friendRepository.findAllByMemberAndFriendAndStatus(friend, me, Status.APPROVE);

        memberValidator.validateFriendRelation(relations, reverse);

        friendRepository.deleteAll(relations);
        friendRepository.deleteAll(reverse);
        return "친구가 삭제되었습니다.";
    }

    @Transactional(readOnly = true)
    public List<FriendRankResponse> getFriendRanking(Long memberId) {
        Member me = findMemberByIdOrThrow(memberId);
        List<Member> targets = friendRepository.findAllByMemberAndStatus(me, Status.APPROVE).stream()
                .map(Friend::getFriend).collect(Collectors.toCollection(ArrayList::new));
        targets.add(me);

        Map<Member, Long> sleepMap = targets.stream()
                .collect(Collectors.toMap(m -> m, sleepCalculator::calculateTotalSleepSeconds));

        List<Member> sorted = targets.stream()
                .sorted((m1, m2) -> sleepMap.get(m2).compareTo(sleepMap.get(m1))).collect(Collectors.toList());

        return IntStream.range(0, sorted.size())
                .mapToObj(i -> MemberConverter.toFriendRankResponse(sorted.get(i), sleepMap.get(sorted.get(i)), i + 1))
                .collect(Collectors.toList());
    }

    public void deleteMeHard(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        memberCleanupManager.deleteAllMemberData(member);
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse.CheckTutorialDTO checkTutorial(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        return MemberResponse.CheckTutorialDTO.builder().finished(member.isTutorialClear()).build();
    }

    @Transactional(readOnly = true)
    public MemberResponse.CompleteTutorialResultDTO completeTutorial(Long memberId, MemberRequestDTO.CompleteTutorialDTO request) {
        Member member = findMemberByIdOrThrow(memberId);
        member.setTutorialClear(request.isFinished());
        return MemberResponse.CompleteTutorialResultDTO.builder()
                .memberId(member.getId()).finished(member.isTutorialClear()).build();
    }

    private Member findMemberByIdOrThrow(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findMemberByNickNameOrThrow(String nickname) {
        return memberRepository.findByNickName(nickname).orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}








