package com.umc_9th.sleepinghero.domain.sleep.repository;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SleepRecordRepository extends JpaRepository<SleepRecord, Long> {

    List<SleepRecord> findAllByMemberAndSuccess(Member member, boolean b);
    Page<SleepRecord> findByMemberId(Long memberId, Pageable pageable);
    Optional<SleepRecord> findByIdAndMemberId(Long id, Long memberId);
    Optional<SleepRecord> findTopByMemberIdAndWokeTimeIsNullOrderBySleptTimeDesc(Long memberId);
    Optional<SleepRecord> findTopByMemberIdAndWokeTimeIsNotNullOrderByWokeTimeDesc(Long memberId);
    List<SleepRecord> findAllByMemberAndIsSuccess(Member member, boolean b);
    List<SleepRecord> findAllByMember(Member member);

    void deleteAllByMemberId(Long memberId);
}
