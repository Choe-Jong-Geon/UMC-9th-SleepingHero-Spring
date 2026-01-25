package com.umc_9th.sleepinghero.domain.member.dto.req;



import lombok.Getter;


public class MemberRequestDTO {

    @Getter
    public static class CompleteTutorialDTO {
        private boolean isFinished;
    }

    @Getter
    public static class UpdateAgreementsDTO {
        private boolean marketingAgreed;
    }

}
