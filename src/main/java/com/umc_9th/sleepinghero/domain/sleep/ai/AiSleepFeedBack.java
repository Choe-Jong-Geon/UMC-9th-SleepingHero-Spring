package com.umc_9th.sleepinghero.domain.sleep.ai;

import java.util.List;

public record AiSleepFeedBack(
        String summary,
        List<String> positives,
        List<String> improvements,
        String cheering
) {
    public static AiSleepFeedBack fallBack() {
        return new AiSleepFeedBack(
                "피드백을 생성하지 못했어요.",
                List.of(),
                List.of(),
                ""
        );

    }
}