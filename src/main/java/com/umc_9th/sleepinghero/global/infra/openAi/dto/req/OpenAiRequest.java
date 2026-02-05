package com.umc_9th.sleepinghero.global.infra.openAi.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OpenAiRequest {

    private String model;
    private List<Message> messages;

    public static OpenAiRequest from(
            String systemPrompt,
            String userPrompt
    ) {
        return new OpenAiRequest(
                "gpt-4.1-mini",
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", userPrompt)
                )
        );
    }

    @Getter
    @AllArgsConstructor
    static class Message {
        private String role;
        private String content;
    }
}
