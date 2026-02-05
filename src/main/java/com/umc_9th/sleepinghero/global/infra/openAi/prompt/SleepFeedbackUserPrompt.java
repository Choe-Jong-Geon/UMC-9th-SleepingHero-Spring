package com.umc_9th.sleepinghero.global.infra.openAi.prompt;

import com.umc_9th.sleepinghero.domain.sleep.ai.SleepFeedBackContext;
import org.springframework.stereotype.Component;

import static org.hibernate.id.uuid.Helper.format;


@Component
public class SleepFeedbackUserPrompt {

    public String build(SleepFeedBackContext req) {
        return """
        SLEEP DATA:
        - Total sleep: %s
        - Goal: %s
        - Streak: %d days
        - Star: %d

        USER REVIEW:
        %s
        """.formatted(
            format(req.sleepDuration()),
            format(req.goalDuration()),
            req.sleepStreak(),
            req.review().star(),
            req.review().comment()
        );
    }
}
