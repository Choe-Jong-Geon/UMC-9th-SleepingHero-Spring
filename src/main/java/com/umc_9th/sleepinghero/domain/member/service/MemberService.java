package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.auth.service.RefreshTokenService;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.group.repository.GroupMemberRepository;
import com.umc_9th.sleepinghero.domain.group.repository.GroupRepository;
import com.umc_9th.sleepinghero.domain.help.repository.HelpRepository;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendRankResponse;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponse;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.skin.repository.SkinMemberRepository;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepFeedBackRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepReviewRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.domain.member.converter.MemberConverter;
import com.umc_9th.sleepinghero.domain.member.dto.res.FriendResponse;
import com.umc_9th.sleepinghero.domain.member.entity.Friend;
import com.umc_9th.sleepinghero.domain.member.repository.FriendRepository;
import com.umc_9th.sleepinghero.global.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final RefreshTokenService refreshTokenService;

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final SleepRecordRepository sleepRecordRepository;
    private final SleepFeedBackRepository sleepFeedBackRepository;
    private final SleepReviewRepository sleepReviewRepository;
    private final SleepGoalRepository sleepGoalRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;

    private final SkinMemberRepository skinMemberRepository;
    private final HeroRepository heroRepository;
    private final HelpRepository helpRepository;

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
                .map(relation -> MemberConverter.toFriendResponse(relation.getMember()))
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

    public List<FriendRankResponse> getFriendRanking(Long memberId) {
        try {
            Member me = findMemberByIdOrThrow(memberId);

            List<Member> rankingTargets = friendRepository.findAllByMemberAndStatus(me, Status.APPROVE)
                    .stream()
                    .map(Friend::getFriend)
                    .collect(Collectors.toCollection(ArrayList::new));
            rankingTargets.add(me);

            Map<Member, Long> memberSleepMap = rankingTargets.stream()
                    .collect(Collectors.toMap(
                            member -> member,
                            this::calculateTotalSleepSeconds
                    ));

            List<Member> sortedMembers = rankingTargets.stream()
                    .sorted((m1, m2) -> memberSleepMap.get(m2).compareTo(memberSleepMap.get(m1)))
                    .collect(Collectors.toList());

            return IntStream.range(0, sortedMembers.size())
                    .mapToObj(i -> {
                        Member m = sortedMembers.get(i);
                        return MemberConverter.toFriendRankResponse(m, memberSleepMap.get(m), i + 1);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new GeneralException(MemberErrorCode.FRIEND_RANKING_ERROR);
        }
    }

    @Transactional
    public void deleteMeHard(Long memberId) {

        refreshTokenService.delete(memberId);

        Member member = findMemberByIdOrThrow(memberId);
        String nickname = member.getNickName();
        List<Group> masterGroups = groupRepository.findAllByMaster(nickname);

        for (Group g : masterGroups) {
            groupMemberRepository.deleteAllByHeroGroups(g);
            groupRepository.delete(g);
        }

        List<GroupMember> approvedGroups =
                groupMemberRepository.findAllByMemberAndStatus(member, Status.APPROVE);

        for (GroupMember gm : approvedGroups) {
            Group g = gm.getHeroGroups();
            if (g.getMaster() != null && g.getMaster().equals(nickname)) continue;
            g.decrementCurrentPeople();
        }

        groupMemberRepository.deleteAllByMember(member);

        List<SleepRecord> records = sleepRecordRepository.findAllByMember(member);
        List<SleepReview> reviews = sleepReviewRepository.findAllBySleepRecordIn(records);
        sleepFeedBackRepository.deleteAllBySleepReviewIn(reviews);
        sleepReviewRepository.deleteAllBySleepRecordIn(records);
        sleepRecordRepository.deleteAllByMemberId(memberId);
        sleepGoalRepository.deleteByMemberId(memberId);
        friendRepository.deleteAllByMemberIdOrFriendId(memberId);
        skinMemberRepository.deleteAllByMemberId(memberId);
        heroRepository.deleteByMemberId(memberId);
        helpRepository.deleteAllByMemberId(memberId);

        memberRepository.delete(member);
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

    public MemberResponse.CheckTutorialDTO checkTutorial(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.CheckTutorialDTO.builder()
                .finished(member.isTutorialClear())
                .build();
    }

    public MemberResponse.CompleteTutorialResultDTO completeTutorial(Long memberId, MemberRequestDTO.CompleteTutorialDTO request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.setTutorialClear(request.isFinished());

//        Member savedMember = memberRepository.save(member);

        return MemberResponse.CompleteTutorialResultDTO.builder()
                .memberId(member.getId())
                .finished(member.isTutorialClear())
                .build();
    }

    private Long calculateTotalSleepSeconds(Member member) {
        return sleepRecordRepository.findAllByMemberAndSuccess(member, true).stream()
                .filter(sr -> sr.getSleptTime() != null && sr.getWokeTime() != null)
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).getSeconds())
                .sum();
    }
}









