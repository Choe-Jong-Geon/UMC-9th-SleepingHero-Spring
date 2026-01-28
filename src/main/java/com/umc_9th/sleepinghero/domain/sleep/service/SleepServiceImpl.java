package com.umc_9th.sleepinghero.domain.sleep.service;


import com.umc_9th.sleepinghero.domain.hero.dto.res.LevelChange;
import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.hero.util.LevelPolicy;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.sleep.converter.SleepConverter;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepEndResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepRecordResponse;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepReward;
import com.umc_9th.sleepinghero.domain.sleep.dto.res.SleepStartResponse;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.exception.SleepErrorCode;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
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
    private final MemberRepository memberRepository;
    private final HeroRepository heroRepository;

    private final SleepConverter sleepConverter;


    @Override
    @Transactional(readOnly = true)
    public Page<SleepRecordResponse> getSleepRecords(int page, int size, Long memberId) {

        PageRequest request = PageRequest.of(page, size);

        Page<SleepRecord> records = sleepRecordRepository.findByMemberId(
                memberId,request
        );

        return records.map(sleepConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public SleepRecordResponse getSleepRecord(Long sleepRecordId, Long memberId) {

        SleepRecord record = getOrThrowSleepRecord(memberId, sleepRecordId);

        return sleepConverter.toDto(record);
    }


    @Override
    @Transactional
    public SleepStartResponse startSleep(Long memberId) {

        Member member = getOrThrowMember(memberId);

        validateSleepStatus(member);

        SleepGoal goal = getOrThrowGoal(memberId);

        LocalDateTime now = LocalDateTime.now().withNano(0);
        validateSleepTime(now, goal.getSleepTime());

        SleepRecord record = SleepRecord.builder()
                .sleptTime(LocalDateTime.now().withNano(0))
                .member(member)
                .build();

        sleepRecordRepository.save(record);

        member.startSleep();

        return sleepConverter.toDto(record,member.isSleepStatus());
    }


    @Override
    @Transactional
    public SleepEndResponse endSleep(Long memberId) {

        Member member = getOrThrowMember(memberId);

        if(!member.isSleepStatus()) {
            throw new GeneralException(SleepErrorCode.SLEEP_NOT_IN_PROGRESS);
        }

        Hero hero = getOrThrowHero(memberId);

        SleepRecord record = getOrThrowSleepRecord(memberId);

        SleepGoal goal = getOrThrowGoal(memberId);

        LocalDateTime now = LocalDateTime.now().withNano(0);
        record.updateWokeTime(now);
        applyGoalResult(goal,record, now); // 목표 기상시간보다 10분 이상 일찍 일어나면 실패

        member.endSleep();

        Duration d = Duration.between(record.getSleptTime(), record.getWokeTime());

        SleepReward reward = getSleepReward(hero, d);

        return sleepConverter.toDto(
                record,
                d.toMinutes(),
                reward,
                hero.getCurrentStage()
        );
    }


    // ------------------------- private -----------------------------

    // ------------------------------ 조회 로직 ------------------------------


    private Member getOrThrowMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private SleepGoal getOrThrowGoal(Long memberId) {
        return sleepGoalRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_GOAL_NOT_FOUND));
    }

    private SleepRecord getOrThrowSleepRecord(Long memberId, Long sleepRecordId) {
        return sleepRecordRepository.findByIdAndMemberId(sleepRecordId, memberId)
                .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_RECORD_NOT_FOUND));
    }

    // 종료되지 않은 제일 최신 기록 조회
    private SleepRecord getOrThrowSleepRecord(Long memberId) {
        return sleepRecordRepository
                .findTopByMemberIdAndWokeTimeIsNullOrderBySleptTimeDesc(memberId)
                .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_SESSION_INCONSISTENT));
    }

    private Hero getOrThrowHero(Long memberId) {
        return heroRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(HeroErrorCode.HERO_NOT_FOUND));
    }

    // 보상 생성
    private SleepReward getSleepReward(Hero hero, Duration duration) {

        int gainedExp = calculateGainedExp(duration);

        LevelChange level = updateLevel(hero,gainedExp);

        return new SleepReward(gainedExp, level);
    }


    // ------------------------------ 상태 변경 로직 ------------------------------

    // 수면 목표 결과 처리
    private void applyGoalResult(SleepGoal goal, SleepRecord record, LocalDateTime now){

        LocalDateTime slept = record.getSleptTime();
        LocalDateTime goalDateTime = LocalDateTime.of(slept.toLocalDate(), goal.getWakeTime());

        // 목표시간이 다음날 아침에 일어나는 경우
        if (goal.getWakeTime().isBefore(slept.toLocalTime())) {
            goalDateTime = goalDateTime.plusDays(1);
        }

        LocalDateTime failTime = goalDateTime.minusMinutes(10);

        if(failTime.isBefore(now))  // 목표시간 - 10분 이후에 기상한 경우
            goal.successGoal();
        else
            goal.failGoal();

    }

    // 레벨 변경
    private LevelChange updateLevel(Hero hero, int gainedExp) {
        int prevLevel = hero.getCurrentLevel();

        hero.gainExp(gainedExp);

        int currentLevel = hero.getCurrentLevel();

        return new LevelChange(
                prevLevel,
                currentLevel,
                hero.getCurrentExp(),
                LevelPolicy.needExp(currentLevel)
        );
    }


    // ------------------------------ 검증 로직 ------------------------------

    // 수면 시작 시간 내 요청 검증
    private void validateSleepTime(LocalDateTime now, LocalTime goalSleepTime){
        if( calculateSleepTime(now, goalSleepTime) > 10)
            throw new GeneralException(SleepErrorCode.SLEEP_GOAL_INVALID);

    }

    //
    private void validateSleepStatus(Member member){
        if(member.isSleepStatus())
            throw new GeneralException(SleepErrorCode.SLEEP_ALREADY_IN_PROGRESS);
    }


    // ------------------------------ 계산 로직 ------------------------------

    // 허용 범위 내 시간 계산
    private long calculateSleepTime(LocalDateTime now, LocalTime goalTime) {

        LocalDateTime today = LocalDateTime.of(now.toLocalDate(), goalTime);
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime tomorrow = today.plusDays(1);

        return Math.min(
                Duration.between(now, yesterday).abs().toMinutes()
                ,Math.min(
                        Duration.between(now, today).abs().toMinutes(),
                        Duration.between(now, tomorrow).abs().toMinutes()
                ));
    }

    // 획득 경험치 계산 로직
    private int calculateGainedExp(Duration d) {
        long m = d.toMinutes(); // 총 분

        if (m <= 4 * 60) return 0;
        if (m <= 5 * 60) return 20;
        if (m <= 6 * 60) return 40;
        if (m <= 7 * 60) return 60;
        if (m <= 8 * 60) return 80;
        return 100;
    }


}


