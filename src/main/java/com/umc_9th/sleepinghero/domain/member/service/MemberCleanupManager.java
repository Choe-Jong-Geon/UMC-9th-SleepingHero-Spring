package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.auth.service.RefreshTokenService;
import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.repository.GroupMemberRepository;
import com.umc_9th.sleepinghero.domain.group.repository.GroupRepository;
import com.umc_9th.sleepinghero.domain.help.repository.HelpRepository;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.repository.FriendRepository;
import com.umc_9th.sleepinghero.domain.skin.repository.SkinMemberRepository;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepFeedBackRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepReviewRepository;
import com.umc_9th.sleepinghero.global.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberCleanupManager {
    private final RefreshTokenService refreshTokenService;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final SleepRecordRepository sleepRecordRepository;
    private final SleepReviewRepository sleepReviewRepository;
    private final SleepFeedBackRepository sleepFeedBackRepository;
    private final SleepGoalRepository sleepGoalRepository;
    private final FriendRepository friendRepository;
    private final SkinMemberRepository skinMemberRepository;
    private final HeroRepository heroRepository;
    private final HelpRepository helpRepository;

    public void deleteAllMemberData(Member member) {
        Long memberId = member.getId();
        String nickname = member.getNickName();

        refreshTokenService.delete(memberId);
        handleGroupCleanup(member, nickname);
        handleSleepCleanup(member, memberId);

        friendRepository.deleteAllByMemberIdOrFriendId(memberId);
        skinMemberRepository.deleteAllByMemberId(memberId);
        heroRepository.deleteByMemberId(memberId);
        helpRepository.deleteAllByMemberId(memberId);
    }

    private void handleGroupCleanup(Member member, String nickname) {
        List<Group> masterGroups = groupRepository.findAllByMaster(nickname);
        for (Group g : masterGroups) {
            groupMemberRepository.deleteAllByHeroGroups(g);
            groupRepository.delete(g);
        }

        groupMemberRepository.findAllByMemberAndStatus(member, Status.APPROVE).forEach(gm -> {
            Group g = gm.getHeroGroups();
            if (g.getMaster() == null || !g.getMaster().equals(nickname)) {
                g.decrementCurrentPeople();
            }
        });
        groupMemberRepository.deleteAllByMember(member);
    }

    private void handleSleepCleanup(Member member, Long memberId) {
        List<SleepRecord> records = sleepRecordRepository.findAllByMember(member);
        List<SleepReview> reviews = sleepReviewRepository.findAllBySleepRecordIn(records);
        sleepFeedBackRepository.deleteAllBySleepReviewIn(reviews);
        sleepReviewRepository.deleteAllBySleepRecordIn(records);
        sleepRecordRepository.deleteAllByMemberId(memberId);
        sleepGoalRepository.deleteByMemberId(memberId);
    }
}
