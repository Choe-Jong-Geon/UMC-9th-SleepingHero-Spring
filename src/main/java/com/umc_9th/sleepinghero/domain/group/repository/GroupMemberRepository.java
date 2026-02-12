package com.umc_9th.sleepinghero.domain.group.repository;

import com.umc_9th.sleepinghero.domain.group.entity.Group;
import com.umc_9th.sleepinghero.domain.group.entity.GroupMember;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.global.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findAllByHeroGroupsIdAndStatus(Long groupId, Status status);

    boolean existsByMemberAndHeroGroups(Member invitee, Group group);

    Optional<GroupMember> findByMemberAndHeroGroupsAndStatus(Member me, Group group, Status status);

    List<GroupMember> findAllByHeroGroupsAndStatus(Group group, Status status);

    List<GroupMember> findAllByMemberAndStatus(Member member, Status status);

    void deleteAllByMember(Member member);

    void deleteAllByHeroGroups(Group group);
}
