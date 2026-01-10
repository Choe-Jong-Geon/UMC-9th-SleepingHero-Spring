package com.umc_9th.sleepinghero.domain.skin.entity;

import com.umc_9th.sleepinghero.domain.badge.entity.Badge;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
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
@Table(name = "skin_member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "skin_id"})
        })
@Builder
public class SkinMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id", nullable = false)
    private Skin skin;

}
