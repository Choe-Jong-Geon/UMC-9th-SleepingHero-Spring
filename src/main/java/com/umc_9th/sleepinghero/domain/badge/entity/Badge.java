package com.umc_9th.sleepinghero.domain.badge.entity;

import com.umc_9th.sleepinghero.domain.badge.enums.BadgeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "badges")
@Builder
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "badge_type")
    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;

    @Column(nullable = false)
    private int badgeValue;

}
