package com.umc_9th.sleepinghero.domain.help.entity;

import com.umc_9th.sleepinghero.domain.help.enums.HelpType;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "helps")
public class Help extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelpType type; // BUG, SUGGESTION

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String responseEmail;

}