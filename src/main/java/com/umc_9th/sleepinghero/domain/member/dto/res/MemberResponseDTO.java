package com.umc_9th.sleepinghero.domain.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
public class MemberResponseDTO {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteTutorialResultDTO {
        private Long memberId;
        private boolean isFinished;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckTutorialDTO {
        private boolean isFinished;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgreementsDTO {
        private boolean serviceTerms;   // 이용약관 (무조건 true)
        private boolean privacyTerms;   // 개인정보 (무조건 true)
        private boolean marketingTerms; // 마케팅

    }
}
