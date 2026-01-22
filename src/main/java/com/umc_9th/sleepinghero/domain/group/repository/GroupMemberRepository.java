package com.umc_9th.sleepinghero.domain.group.repository;

import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.global.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findAllByHeroGroupsIdAndStatus(Long groupId, Status status);
}
