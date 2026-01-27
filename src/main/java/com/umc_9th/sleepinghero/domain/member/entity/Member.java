package com.umc_9th.sleepinghero.domain.member.entity;

import com.umc_9th.sleepinghero.domain.member.enums.OauthProvider;
import com.umc_9th.sleepinghero.domain.member.enums.Role;
import com.umc_9th.sleepinghero.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Members")
@Builder
public class Member extends BaseEntity {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickName;

    private String profilePicture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthProvider provider;

    @Column(nullable = false, name = "provider_id")
    private String providerId;

    @Setter
    @Column(nullable = false, name = "tutorial_clear")
    @Builder.Default
    private boolean tutorialClear = false;

    @Column(nullable = false, name = "sleep_status")
    @Builder.Default
    private boolean sleepStatus = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
