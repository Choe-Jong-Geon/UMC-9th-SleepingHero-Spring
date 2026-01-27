package com.umc_9th.sleepinghero.domain.help.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HelpResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryUrlDTO {
        private String url; // 딥링크 주소
    }
}
