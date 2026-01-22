package com.umc_9th.sleepinghero.domain.sleep.Repository;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SleepRecordRepository extends JpaRepository<SleepRecord, Long> {
    List<SleepRecord> findAllByMemberAndSuccess(Member member, boolean b);
}
