package com.umc_9th.sleepinghero.global.infra.openAi.dto.res;

import lombok.Getter;

import java.util.List;

@Getter
public class OpenAiResponse {

    private List<Choice> choices;
    private Usage usage;            // 토큰 사용량 추적

    public String getContent() {
        return choices.getFirst().getMessage().getContent();
    }

    public int getTotalTokens() {
        return usage.getTotalTokens();
    }

    @Getter
    static class Choice {
        private Message message;
    }

    @Getter
    static class Message {
        private String content;
    }

    @Getter
    static class Usage {
        private int totalTokens;
    }
}
