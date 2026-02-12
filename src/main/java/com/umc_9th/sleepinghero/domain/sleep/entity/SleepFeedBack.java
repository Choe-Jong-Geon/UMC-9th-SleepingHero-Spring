package com.umc_9th.sleepinghero.domain.sleep.entity;

import com.umc_9th.sleepinghero.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sleep_feedbacks")
public class SleepFeedBack extends BaseEntity {

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String cheering;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sleep_review_id", nullable = false, unique = true)
    private SleepReview sleepReview;

    @OneToMany(mappedBy = "sleepFeedBack",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<SleepFeedbackImprovement> improvements = new ArrayList<>();

    @OneToMany(mappedBy = "sleepFeedBack",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<SleepFeedbackPositive> positives = new ArrayList<>();

    public void addImprovement(String content) {
        SleepFeedbackImprovement imp =
                new SleepFeedbackImprovement(content, this);
        improvements.add(imp);
    }

    public void addPositive(String content) {
        SleepFeedbackPositive pos =
                new SleepFeedbackPositive(content, this);
        positives.add(pos);
    }
}
