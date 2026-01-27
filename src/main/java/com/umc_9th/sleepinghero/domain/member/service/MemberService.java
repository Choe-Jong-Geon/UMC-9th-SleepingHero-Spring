package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponseDTO;
import com.umc_9th.sleepinghero.domain.member.entity.Member;

public interface MemberService {

    MemberResponseDTO.CheckTutorialDTO checkTutorial(Long memberId);

    MemberResponseDTO.CompleteTutorialResultDTO completeTutorial(Long memberId, MemberRequestDTO.CompleteTutorialDTO request);

    MemberResponseDTO.AgreementsDTO  getAgreements(Long memberId);

    MemberResponseDTO.AgreementsDTO updateAgreements(Long memberId, MemberRequestDTO.UpdateAgreementsDTO request);
}
