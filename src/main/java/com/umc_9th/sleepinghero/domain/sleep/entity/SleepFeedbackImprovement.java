package com.umc_9th.sleepinghero.domain.sleep.entity;

import com.umc_9th.sleepinghero.global.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "sleep_feedback_improvements")
public class SleepFeedbackImprovement extends BaseEntity {

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sleep_feedback_id", nullable = false)
    private SleepFeedBack sleepFeedBack;

    protected SleepFeedbackImprovement() {}

    public SleepFeedbackImprovement(String content, SleepFeedBack sleepFeedBack) {
        this.content = content;
        this.sleepFeedBack = sleepFeedBack;
    }
}
