package com.umc_9th.sleepinghero.domain.sleepGoal.entity;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sleep_goals")
@Builder
public class SleepGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "sleep_time")
    private LocalTime sleepTime;

    @Column(nullable = false, name = "wake_time")
    private LocalTime wakeTime;

    @Column(nullable = false, name = "current_streak")
    @Builder.Default
    private int currentStreak = 0;

    @Column(nullable = false, name = "best_streak")
    @Builder.Default
    private int bestStreak = 0;

    @Column(nullable = false, name = "non_sleep_streak")
    @Builder.Default
    private int nonSleepStreak = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
