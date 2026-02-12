package com.umc_9th.sleepinghero.domain.sleep.entity;

import com.umc_9th.sleepinghero.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ElementCollection
    @CollectionTable(
            name = "sleep_feedback_positives",
            joinColumns = @JoinColumn(name = "sleep_feedback_id")
    )
    @Column(nullable = false)
    private List<String> positives;

    @ElementCollection
    @CollectionTable(
            name = "sleep_feedback_improvements",
            joinColumns = @JoinColumn(name = "sleep_feedback_id")
    )
    @Column(nullable = false)
    private List<String> improvements;

    @Column(nullable = false)
    private String cheering;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sleep_review_id", nullable = false, unique = true)
    private SleepReview sleepReview;

}
