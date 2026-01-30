package com.umc_9th.sleepinghero.domain.home.service;

import com.umc_9th.sleepinghero.domain.home.dto.res.DashBoardResponse;

public interface HomeService {
    DashBoardResponse dashboard(Long memberId);
}
