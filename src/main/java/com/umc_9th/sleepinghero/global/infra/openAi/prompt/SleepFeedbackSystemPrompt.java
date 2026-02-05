package com.umc_9th.sleepinghero.global.infra.openAi.prompt;

import org.springframework.stereotype.Component;

@Component
public class SleepFeedbackSystemPrompt {

    public String value() {
        return """
        You are a sleep coaching assistant for a health app.

        RULES:
        - Respond in Korean
        - Do NOT give medical advice
        - Be concise and actionable

        OUTPUT FORMAT:
        - Respond ONLY in valid JSON.
        {
          "summary": "string",
          "positives": ["string"],
          "improvements": ["string"],
          "cheering" : "string"
        }
        """;
    }
}
