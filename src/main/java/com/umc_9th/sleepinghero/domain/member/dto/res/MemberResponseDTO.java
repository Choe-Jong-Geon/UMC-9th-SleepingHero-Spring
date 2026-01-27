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
        private boolean finished;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckTutorialDTO {
        private boolean finished;
    }

}
