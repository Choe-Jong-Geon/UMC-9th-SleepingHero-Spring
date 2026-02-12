package com.umc_9th.sleepinghero.domain.group.calculator;

import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.group.repository.GroupMemberRepository;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepRecordRepository;
import com.umc_9th.sleepinghero.global.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupStatisticsCalculator {

    private final SleepRecordRepository sleepRecordRepository;
    private final GroupMemberRepository groupMemberRepository;

    public long calculateTotalSleepSeconds(Member member) {
        return sleepRecordRepository.findAllByMemberAndIsSuccess(member, true).stream()
                .filter(sr -> sr.getSleptTime() != null && sr.getWokeTime() != null)
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).getSeconds())
                .sum();
    }

    public int calculateConsecutiveDays(Member member) {
        List<LocalDate> dates = sleepRecordRepository.findAllByMemberAndIsSuccess(member, true).stream()
                .map(sr -> sr.getSleptTime().toLocalDate())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (dates.isEmpty()) return 0;

        LocalDate target = dates.get(0);
        if (!target.equals(LocalDate.now()) && !target.equals(LocalDate.now().minusDays(1))) return 0;

        int count = 0;
        for (LocalDate d : dates) {
            if (d.equals(target)) {
                count++;
                target = target.minusDays(1);
            } else break;
        }
        return count;
    }

    public double calculateGroupSleepAverage(Long groupId) {
        List<GroupMember> gms = groupMemberRepository.findAllByHeroGroupsIdAndStatus(groupId, Status.APPROVE);
        return gms.stream()
                .map(GroupMember::getMember)
                .flatMap(m -> sleepRecordRepository.findAllByMemberAndIsSuccess(m, true).stream())
                .mapToLong(sr -> Duration.between(sr.getSleptTime(), sr.getWokeTime()).toMinutes())
                .average().orElse(0.0);
    }
}
