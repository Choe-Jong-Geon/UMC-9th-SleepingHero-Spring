package com.umc_9th.sleepinghero.domain.hero.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HeroRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class UpdateNameDTO {
        @NotBlank(message = "변경할 이름은 필수입니다.")
        @Size(min = 2, max = 10)
        private String name;
    }
}