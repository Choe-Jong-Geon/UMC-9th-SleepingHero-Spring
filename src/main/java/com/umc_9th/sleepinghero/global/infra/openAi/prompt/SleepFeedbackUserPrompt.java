package com.umc_9th.sleepinghero.global.infra.openAi.prompt;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBackContext;
import org.springframework.stereotype.Component;

@Component
public class SleepFeedbackUserPrompt {

    public String build(AiSleepFeedBackContext req) {
        return """
        SLEEP DATA:
        - Total sleep: %d minutes
        - Goal: %d minutes
        - Streak: %d days
        - Star: %d

        USER REVIEW:
        %s
        """.formatted(
                req.sleepDuration(),
                req.goalDuration(),
                req.sleepStreak(),
                req.star(),
                req.comment()
        );
    }
}
