package com.umc_9th.sleepinghero.domain.sleep.calculator;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SleepCalculator {

    private final SleepRecordRepository sleepRecordRepository;

    public Long calculateTotalSleepSeconds(Member member) {
        return sleepRecordRepository.findAllByMemberAndIsSuccess(member, true).stream()
                .filter(sr -> sr.getSleptTime() != null && sr.getWokeTime() != null)
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).getSeconds())
                .sum();
    }


    public int toHours(Long seconds) {
        if (seconds == null) return 0;
        return (int) (seconds / 3600);
    }
}
