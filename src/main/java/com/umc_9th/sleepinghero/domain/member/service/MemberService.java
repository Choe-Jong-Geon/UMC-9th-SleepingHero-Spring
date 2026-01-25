package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponseDTO;
import com.umc_9th.sleepinghero.domain.member.entity.Member;

public interface MemberService {

    MemberResponseDTO.CheckTutorialDTO checkTutorial(Member member);

    MemberResponseDTO.CompleteTutorialResultDTO completeTutorial(Member member, MemberRequestDTO.CompleteTutorialDTO request);

    MemberResponseDTO.AgreementsDTO  getAgreements(Member member);

    MemberResponseDTO.AgreementsDTO updateAgreements(Member member, MemberRequestDTO.UpdateAgreementsDTO request);
}
