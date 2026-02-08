package com.umc_9th.sleepinghero.domain.help.service;

import com.umc_9th.sleepinghero.domain.help.dto.req.HelpRequestDTO;
import com.umc_9th.sleepinghero.domain.help.dto.res.HelpResponseDTO;

public interface HelpService {
    void createHelpInquiry(Long memberId, HelpRequestDTO.CreateHelpDTO request);

}
