package com.umc_9th.sleepinghero.domain.hero.entity;

import com.umc_9th.sleepinghero.domain.hero.exception.HeroErrorCode;
import com.umc_9th.sleepinghero.domain.hero.util.LevelPolicy;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.skin.entity.Skin;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
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
@Table(name = "heroes")
@Builder
public class Hero extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "current_level")
    private int currentLevel;

    @Column(nullable = false, name = "current_exp")
    private int currentExp;

    @Column(nullable = false, name = "current_stage")
    private int currentStage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skin_id", nullable = false)
    private Skin currentSkin;

    public void updateName(String name) {
        this.name = name;
    }
    public void updateSkin(Skin skin) {
        this.currentSkin = skin;
    }

    public void gainExp(int gainedExp) {

        int level = this.currentLevel;
        int exp = this.currentExp + gainedExp;

        if (level < 1 || level > LevelPolicy.getMax())
            throw new GeneralException(HeroErrorCode.INVALID_LEVEL_STATE);

        while (level < LevelPolicy.getMax()) {
            int need = LevelPolicy.needExp(level); // 다음 레벨 필요한 exp
            if (exp < need) break;

            exp -= need;
            level++;
        }

        this.currentLevel = level;
        this.currentExp = exp;
        this.currentStage += 1;

    }


}
