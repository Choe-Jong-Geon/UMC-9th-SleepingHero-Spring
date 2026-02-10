package com.umc_9th.sleepinghero.domain.hero.repository;

import com.umc_9th.sleepinghero.domain.hero.entity.Hero;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HeroRepository extends JpaRepository<Hero, Long> {

    Optional<Hero> findByMemberId(Long memberId);
    Optional<Hero> findByName(String name);
    boolean existsByName(String name);

    Optional<Hero> findByMember(Member member);
    long deleteByMemberId(Long memberId);
}