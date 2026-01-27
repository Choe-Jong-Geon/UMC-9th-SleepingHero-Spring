package com.umc_9th.sleepinghero.domain.skin.repository;

import com.umc_9th.sleepinghero.domain.skin.entity.SkinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SkinMemberRepository extends JpaRepository<SkinMember, Long> {

    // 유저 ID로 보유한 스킨 관계 목록 전체 조회
    List<SkinMember> findAllByMemberId(Long memberId);

    // 유저 ID와 스킨 ID로 보유 여부 확인
    Optional<SkinMember> findByMemberIdAndSkinId(Long memberId, Long skinId);
}