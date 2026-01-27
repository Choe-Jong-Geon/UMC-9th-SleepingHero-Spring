package com.umc_9th.sleepinghero.domain.skin.repository;


import com.umc_9th.sleepinghero.domain.skin.entity.Skin;
import com.umc_9th.sleepinghero.domain.skin.entity.SkinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkinRepository extends JpaRepository<Skin, Long> {

}
