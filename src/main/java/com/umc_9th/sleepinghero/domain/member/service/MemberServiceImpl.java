package com.umc_9th.sleepinghero.domain.member.service;

import com.umc_9th.sleepinghero.domain.member.dto.req.MemberRequestDTO;
import com.umc_9th.sleepinghero.domain.member.dto.res.MemberResponseDTO;
import com.umc_9th.sleepinghero.domain.member.entity.Member;
import com.umc_9th.sleepinghero.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    @Override
    public MemberResponseDTO.CheckTutorialDTO checkTutorial(Member member) {
        return MemberResponseDTO.CheckTutorialDTO.builder()
                .isFinished(member.isTutorialClear())
                .build();
    }


    @Override
    @Transactional
    public MemberResponseDTO.CompleteTutorialResultDTO completeTutorial(Member member, MemberRequestDTO.CompleteTutorialDTO request) {


        member.setTutorialClear(request.isFinished());


        Member savedMember = memberRepository.save(member);

        return MemberResponseDTO.CompleteTutorialResultDTO.builder()
                .memberId(savedMember.getId())
                .isFinished(savedMember.isTutorialClear())
                .build();
    }

    @Override
    public MemberResponseDTO.AgreementsDTO getAgreements(Member member) {
        return MemberResponseDTO.AgreementsDTO.builder()
                .serviceTerms(true)
                .privacyTerms(true)
                .marketingTerms(member.isMarketingAgreed())
                .build();
    }


    @Override
    @Transactional
    public MemberResponseDTO.AgreementsDTO updateAgreements(Member member, MemberRequestDTO.UpdateAgreementsDTO request) {


        member.setMarketingAgreed(request.isMarketingAgreed());


        Member savedMember = memberRepository.save(member);

        return MemberResponseDTO.AgreementsDTO.builder()
                .serviceTerms(true)
                .privacyTerms(true)
                .marketingTerms(savedMember.isMarketingAgreed())
                .build();
    }


}
