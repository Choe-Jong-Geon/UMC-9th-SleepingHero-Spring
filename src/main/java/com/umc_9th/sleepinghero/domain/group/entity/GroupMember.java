package com.umc_9th.sleepinghero.domain.group.entity;

import com.umc_9th.sleepinghero.domain.group.enums.GroupRole;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.global.entity.BaseEntity;
import com.umc_9th.sleepinghero.global.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_member ",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "group_id"})
    })
@Builder
public class GroupMember extends BaseEntity {

    @Column(nullable = false, name = "group_role")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GroupRole groupRole = GroupRole.USER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
