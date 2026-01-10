package com.umc_9th.sleepinghero.domain.sleep.entity;

import com.umc_9th.sleepinghero.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sleep_reviews")
@Builder
public class SleepReview extends BaseEntity {

    private int star;

    private String comment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sleep_record_id", nullable = false, unique = true)
    private SleepRecord sleepRecord;
}
