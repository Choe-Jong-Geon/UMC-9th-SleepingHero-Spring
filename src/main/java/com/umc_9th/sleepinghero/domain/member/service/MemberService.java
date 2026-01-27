package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponseDTO;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.exception.MemberErrorCode;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;



    public MemberResponseDTO.CheckTutorialDTO checkTutorial(Long memberId) {


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        return MemberResponseDTO.CheckTutorialDTO.builder()
                .finished(member.isTutorialClear())
                .build();
    }



    @Transactional
    public MemberResponseDTO.CompleteTutorialResultDTO completeTutorial(Long memberId, MemberRequestDTO.CompleteTutorialDTO request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.setTutorialClear(request.isFinished());

//        Member savedMember = memberRepository.save(member);

        return MemberResponseDTO.CompleteTutorialResultDTO.builder()
                .memberId(member.getId())
                .finished(member.isTutorialClear())
                .build();
    }


    public MemberResponseDTO.AgreementsDTO getAgreements(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        return MemberResponseDTO.AgreementsDTO.builder()
                .serviceTerms(true)
                .privacyTerms(true)
                .marketingTerms(member.isMarketingAgreed())
                .build();
    }



    @Transactional
    public MemberResponseDTO.AgreementsDTO updateAgreements(Long memberId, MemberRequestDTO.UpdateAgreementsDTO request) {


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.setMarketingAgreed(request.isMarketingAgreed());

        Member savedMember = memberRepository.save(member);

        return MemberResponseDTO.AgreementsDTO.builder()
                .serviceTerms(true)
                .privacyTerms(true)
                .marketingTerms(savedMember.isMarketingAgreed())
                .build();
    }

}
