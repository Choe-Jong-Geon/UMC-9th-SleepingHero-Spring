package com.umc_9th.sleepinghero.global.infra.openAi;

import com.umc_9th.sleepinghero.global.infra.openAi.dto.req.OpenAiRequest;
import com.umc_9th.sleepinghero.global.infra.openAi.dto.res.OpenAiResponse;
import com.umc_9th.sleepinghero.global.infra.openAi.prompt.SleepFeedbackSystemPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final WebClient openAiWebClient;
    private final SleepFeedbackSystemPrompt systemPrompt;

    public OpenAiResponse chat(OpenAiRequest request) {
        return openAiWebClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .block();
    }
}
