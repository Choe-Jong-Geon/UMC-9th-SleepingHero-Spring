package com.umc_9th.sleepinghero.domain.skin.entity;


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
@Table(name = "skins")
@Builder
public class Skin extends BaseEntity {

    private String name;
}
