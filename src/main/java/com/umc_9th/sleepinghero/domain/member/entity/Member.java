package com.umc_9th.sleepinghero.domain.member.entity;

import com.umc_9th.sleepinghero.domain.member.enums.Role;
import com.umc_9th.sleepinghero.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Members")
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickName;

    private String profilePicture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Column(nullable = false, unique = true, name = "provider_id")
    private String providerId;

    @Column(nullable = false, name = "tutorial_clear")
    @Builder.Default
    private boolean tutorialClear = false;

    @Column(nullable = false, name = "sleep_status")
    @Builder.Default
    private boolean sleepStatus = false;

    @CreatedBy
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    private LocalDateTime updatedAt;
}
