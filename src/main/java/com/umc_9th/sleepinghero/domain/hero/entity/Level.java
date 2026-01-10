package com.umc_9th.sleepinghero.domain.hero.entity;

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
@Table(name = "levels")
@Builder
public class Level extends BaseEntity {

        @Column(nullable = false)
        private int needExp;

}
