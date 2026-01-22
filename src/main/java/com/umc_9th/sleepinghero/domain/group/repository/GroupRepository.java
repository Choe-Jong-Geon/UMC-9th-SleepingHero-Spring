package com.umc_9th.sleepinghero.domain.group.repository;

import com.umc_9th.sleepinghero.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    boolean existsByName(String name);

    @Query("SELECT g FROM Group g")
    List<Group> findAllGroups();
}
