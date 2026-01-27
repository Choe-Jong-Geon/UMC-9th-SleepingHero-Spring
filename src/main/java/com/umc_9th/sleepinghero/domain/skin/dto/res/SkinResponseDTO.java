package com.umc_9th.sleepinghero.domain.skin.dto.res;

import lombok.*;
import java.util.List;

public class SkinResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkinInfoDTO {
        private Long skinId;
        private String name;
        private boolean isEquipped; // 현재 내 캐릭터가 입고 있는지 여부
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkinListDTO {
        private List<SkinInfoDTO> skins;
    }
}