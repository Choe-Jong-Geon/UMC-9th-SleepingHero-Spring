package com.umc_9th.sleepinghero.domain.member.repository;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
