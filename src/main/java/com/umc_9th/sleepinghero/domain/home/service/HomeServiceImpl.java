package com.umc_9th.sleepinghero.domain.home.service;

import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.hero.repository.HeroRepository;
import com.umc_9th.sleepinghero.domain.home.converter.HomeConverter;
import com.umc_9th.sleepinghero.domain.home.dto.res.DashBoardResponse;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepGoal;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import com.umc_9th.sleepinghero.domain.sleep.exception.SleepErrorCode;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepGoalRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HeroRepository heroRepository;
    private final SleepGoalRepository sleepGoalRepository;
    private final HomeConverter homeConverter;

    @Override
    public DashBoardResponse dashboard(Long memberId) {

        Hero hero = heroRepository.findByMemberId(memberId)
                .orElseThrow(RuntimeException::new);

        SleepGoal goal = sleepGoalRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(SleepErrorCode.SLEEP_GOAL_NOT_FOUND));

        return homeConverter.toDto(hero, goal);
    }
}
