package com.umc_9th.sleepinghero.domain.member.repository;

import com.umc_9th.sleepinghero.domain.member.entity.Friend;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.global.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {


    boolean existsByMemberAndFriend(Member me, Member friend);

    Optional<Friend> findByMemberAndFriendAndStatus(Member member, Member friend, Status status);

    List<Friend> findAllByMemberAndStatus(Member me, Status status);

    List<Friend> findAllByMemberAndFriendAndStatus(Member me, Member friend, Status status);

    @Modifying
    @Query("""
    delete from Friend f
    where f.member.id = :memberId or f.friend.id = :memberId
""")
    void deleteAllByMemberIdOrFriendId(@Param("memberId") Long memberId);

    List<Friend> findAllByFriendAndStatus(Member friend, Status status);
}
