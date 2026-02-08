package com.umc_9th.sleepinghero.domain.help.dto.req;

import com.umc_9th.sleepinghero.domain.help.enums.HelpType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HelpRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateHelpDTO {

        @NotNull(message = "문의 유형(BUG 또는 SUGGESTION)은 필수입니다.")
        private HelpType type;

        @NotBlank(message = "문의 내용은 비어둘 수 없습니다.")
        @Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
        private String content;

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String responseEmail;
    }
}