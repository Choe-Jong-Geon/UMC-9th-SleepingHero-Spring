package com.umc_9th.sleepinghero.domain.member.repository;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

   Optional<Member> findByNickName(String nickName);

}
