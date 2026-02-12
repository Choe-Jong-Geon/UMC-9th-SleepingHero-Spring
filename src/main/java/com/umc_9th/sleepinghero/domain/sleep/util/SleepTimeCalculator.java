package com.umc_9th.sleepinghero.domain.sleep.util;

import java.time.*;

public class SleepTimeCalculator {

    // 수면 시작 허용 범위 계산
    public static long calculateNearestGoalTimeMinutes(LocalDateTime now, LocalTime goalTime) {

        LocalDateTime today = LocalDateTime.of(now.toLocalDate(), goalTime);
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime tomorrow = today.plusDays(1);

        return Math.min(
                Duration.between(now, yesterday).abs().toMinutes(),
                Math.min(
                        Duration.between(now, today).abs().toMinutes(),
                        Duration.between(now, tomorrow).abs().toMinutes()
                )
        );
    }

    // 목표 수면 시간 계산
    public static long durationMinutes(LocalTime start, LocalTime end) {

        LocalDate today = LocalDate.now();

        LocalDateTime startDT = LocalDateTime.of(today, start);
        LocalDateTime endDT   = LocalDateTime.of(today, end);

        if (end.isBefore(start)) {
            endDT = endDT.plusDays(1);
        }

        return Duration.between(startDT, endDT).toMinutes();
    }
}
