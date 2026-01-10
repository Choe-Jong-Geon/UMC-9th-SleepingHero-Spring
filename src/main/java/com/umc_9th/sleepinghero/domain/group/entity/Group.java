package com.umc_9th.sleepinghero.domain.group.entity;

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
@Table(name = "groups")
@Builder
public class Group extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "max_people ")
    @Builder.Default
    private int maxPeople = 10;


    @Column(nullable = false, name = "current_people ")
    @Builder.Default
    private int currentPeople = 1;

}
