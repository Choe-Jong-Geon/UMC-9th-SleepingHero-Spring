package com.umc_9th.sleepinghero.domain.sleep.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc_9th.sleepinghero.domain.sleep.ai.SleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.ai.SleepFeedBackContext;
import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.global.infra.openAi.OpenAiClient;
import com.umc_9th.sleepinghero.global.infra.openAi.dto.req.OpenAiRequest;
import com.umc_9th.sleepinghero.global.infra.openAi.dto.res.OpenAiResponse;
import com.umc_9th.sleepinghero.global.infra.openAi.prompt.SleepFeedbackSystemPrompt;
import com.umc_9th.sleepinghero.global.infra.openAi.prompt.SleepFeedbackUserPrompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class SleepFeedBackServiceImpl implements SleepFeedBackService {

    private final ObjectMapper objectMapper;

    private final OpenAiClient client;
    private final SleepFeedbackSystemPrompt systemPrompt;
    private final SleepFeedbackUserPrompt userPrompt;

    @Override
    public Object feedback(
            long sleepDuration, long goalDuration, int sleepStreak, SleepReviewRequest request
    ) {

        SleepFeedBackContext context = SleepFeedBackContext.of(
                sleepDuration, goalDuration, sleepStreak, request
        );

        OpenAiRequest openAiRequest = OpenAiRequest.from(
                systemPrompt.value(), userPrompt.build(context)
        );

        OpenAiResponse response = client.chat(openAiRequest);

        try {
            return parseResponse(response);
        }catch (Exception e) {
            return safeParseResponse(response); // 1번만 더 시도 후 fallBack
        }
    }


    // ------------------------------ private ------------------------------


    private SleepFeedBack parseResponse(OpenAiResponse response) {
        return objectMapper.readValue(response.getContent(), SleepFeedBack.class);
    }

    private SleepFeedBack safeParseResponse(OpenAiResponse response) {
        try {
            return objectMapper.readValue(response.getContent(), SleepFeedBack.class);
        }catch (Exception e) {
            return SleepFeedBack.fallBack();
        }
    }
}
