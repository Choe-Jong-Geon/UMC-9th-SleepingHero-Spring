package com.umc_9th.sleepinghero.domain.sleep.service;

import com.umc_9th.sleepinghero.domain.hero.dto.res.LevelChange;
import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.hero.service.HeroService;
import com.umc_9th.sleepinghero.domain.hero.util.LevelPolicy;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.sleep.converter.SleepConverter;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.*;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import com.umc_9th.sleepinghero.domain.sleep.exception.SleepErrorCode;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepFeedBackRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepReviewRepository;
import com.umc_9th.sleepinghero.domain.sleep.util.SleepRewardPolicy;
import com.umc_9th.sleepinghero.domain.sleep.util.SleepTimeCalculator;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class SleepServiceImpl implements SleepService {

    private final SleepRecordRepository sleepRecordRepository;
    private final SleepGoalRepository sleepGoalRepository;
    private final SleepFeedBackRepository sleepFeedBackRepository;
    private final SleepReviewRepository sleepReviewRepository;
    private final MemberRepository memberRepository;
    private final HeroRepository heroRepository;
    private final HeroService heroService;
    private final SleepConverter sleepConverter;

    // ================= 조회 =================

    @Override
    @Transactional(readOnly = true)
    public Page<SleepRecordResponse> getSleepRecords(int page, int size, Long memberId) {

        validateMember(memberId);

        PageRequest request = PageRequest.of(page, size);

        return sleepRecordRepository.findByMemberId(memberId, request)
                .map(this::convertToResponse);
    }


    @Override
    @Transactional(readOnly = true)
    public SleepRecordResponse getSleepRecord(Long sleepRecordId, Long memberId) {

        validateMember(memberId);

        SleepRecord record = getOrThrowSleepRecord(memberId, sleepRecordId);

        return convertToResponse(record);
    }

    // ================= 수면 시작 =================

    @Override
    @Transactional
    public SleepStartResponse startSleep(Long memberId) {

        Member member = getOrThrowMember(memberId);
        validateSleepStatus(member);

        SleepGoal goal = getOrThrowGoal(memberId);

        LocalDateTime now = LocalDateTime.now().withNano(0);

        validateSleepTime(now, goal.getSleepTime());

        SleepRecord record = SleepRecord.builder()
                .sleptTime(now)
                .member(member)
                .build();

        sleepRecordRepository.save(record);

        member.startSleep();

        return sleepConverter.toDto(record, member.isSleepStatus());
    }

    // ================= 수면 종료 =================

    @Override
    @Transactional
    public SleepEndResponse endSleep(Long memberId) {

        Member member = getOrThrowMember(memberId);
        validateNonSleepStatus(member);

        Hero hero = getOrThrowHero(memberId);
        SleepRecord record = getOrThrowNotWakeRecord(memberId);
        SleepGoal goal = getOrThrowGoal(memberId);

        LocalDateTime now = LocalDateTime.now().withNano(0);

        record.updateWokeTime(now);

        boolean isSuccess = goal.evaluateGoal(record, now);

        member.endSleep();

        Duration duration =
                Duration.between(record.getSleptTime(), record.getWokeTime());

        int gainedExp =
                SleepRewardPolicy.calculateGainedExp(duration, goal);

        LevelChange levelChange =
                updateLevel(hero, gainedExp);

        SleepReward reward =
                new SleepReward(gainedExp, isSuccess, levelChange);

        return sleepConverter.toDto(
                record,
                duration.toMinutes(),
                reward,
                hero.getCurrentStage()
        );
    }

    @Override
    public long testRecord(Long memberId) {

        Member member = getOrThrowMember(memberId);

        if(!sleepGoalRepository.existsByMemberId(memberId)) {
            SleepGoal goal = SleepGoal.builder()
                    .member(member)
                    .sleepTime(LocalTime.now().withNano(0))
                    .wakeTime(LocalTime.now().withNano(0).plusHours(3))
                    .build();

            sleepGoalRepository.save(goal);
        }

        SleepRecord record = SleepRecord.builder()
                .sleptTime(LocalDateTime.now().withNano(0))
                .wokeTime(LocalDateTime.now().withNano(0).plusHours(3))
                .isSuccess(true)
                .member(member)
                .build();

        sleepRecordRepository.save(record);

        return record.getId();

    }

    // ================= 레벨 처리 =================

    private LevelChange updateLevel(Hero hero, int gainedExp) {

        int prevLevel = hero.getCurrentLevel();

        hero.gainExp(gainedExp);

        int currentLevel = hero.getCurrentLevel();

        if (currentLevel > prevLevel) {
            heroService.checkAndUnlockSkin(hero.getMember(), currentLevel);
        }

        return new LevelChange(
                prevLevel,
                currentLevel,
                hero.getCurrentExp(),
                LevelPolicy.needExp(currentLevel)
        );
    }

    // ================= 검증 & 조회 =================

    private Member getOrThrowMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private SleepGoal getOrThrowGoal(Long memberId) {
        return sleepGoalRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new GeneralException(SleepErrorCode.SLEEP_GOAL_NOT_FOUND));
    }

    private SleepRecord getOrThrowSleepRecord(Long memberId, Long sleepRecordId) {
        return sleepRecordRepository.findByIdAndMemberId(sleepRecordId, memberId)
                .orElseThrow(() ->
                        new GeneralException(SleepErrorCode.SLEEP_RECORD_NOT_FOUND));
    }

    private SleepRecord getOrThrowNotWakeRecord(Long memberId) {
        return sleepRecordRepository
                .findTopByMemberIdAndWokeTimeIsNullOrderBySleptTimeDesc(memberId)
                .orElseThrow(() ->
                        new GeneralException(SleepErrorCode.SLEEP_SESSION_INCONSISTENT));
    }

    private SleepRecord getOrThrowWakeRecord(Long memberId) {
        return sleepRecordRepository
                .findTopByMemberIdAndWokeTimeIsNotNullOrderByWokeTimeDesc(memberId)
                .orElseThrow(() ->
                        new GeneralException(SleepErrorCode.SLEEP_SESSION_INCONSISTENT));
    }

    private Hero getOrThrowHero(Long memberId) {
        return heroRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new GeneralException(HeroErrorCode.HERO_NOT_FOUND));
    }

    private SleepReview getOrThrowReview(Long sleepRecordId) {
                return sleepReviewRepository.findBySleepRecordId(sleepRecordId)
                        .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_REVIEW_NOT_FOUND));
    }

    private SleepFeedBack getOrThrowFeedback(Long sleepReviewId) {
        return sleepFeedBackRepository.findBySleepReviewId(sleepReviewId)
                .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_FEEDBACK_NOT_FOUND));
    }

    private void validateMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }

    private void validateSleepStatus(Member member) {
        if (member.isSleepStatus()) {
            throw new GeneralException(SleepErrorCode.SLEEP_ALREADY_IN_PROGRESS);
        }
    }

    private void validateNonSleepStatus(Member member) {
        if (!member.isSleepStatus()) {
            throw new GeneralException(SleepErrorCode.SLEEP_NOT_IN_PROGRESS);
        }
    }

    private void validateSleepTime(LocalDateTime now, LocalTime goalSleepTime) {

        long diff =
                SleepTimeCalculator.calculateNearestGoalTimeMinutes(now, goalSleepTime);

        if (diff > 10) {
            throw new GeneralException(SleepErrorCode.SLEEP_GOAL_INVALID);
        }
    }


    // ================= 값 변환 =================
    private SleepRecordResponse convertToResponse(SleepRecord record) {

        SleepReview review = getOrThrowReview(record.getId());
        SleepFeedBack feedBack = getOrThrowFeedback(review.getId());
        long minutes = SleepTimeCalculator.durationMinutes(
                record.getSleptTime().toLocalTime(),record.getWokeTime().toLocalTime()
        );

        return sleepConverter.toDto(record, feedBack, review, minutes);
    }
}
