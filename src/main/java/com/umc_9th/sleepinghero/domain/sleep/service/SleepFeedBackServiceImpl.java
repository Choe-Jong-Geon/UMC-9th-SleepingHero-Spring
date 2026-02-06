package com.umc_9th.sleepinghero.domain.sleep.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBackResponse;
import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBackContext;
import com.umc_9th.sleepinghero.domain.sleep.dto.req.SleepReviewRequest;
import com.umc_9th.sleepinghero.global.infra.openAi.OpenAiClient;
import com.umc_9th.sleepinghero.global.infra.openAi.dto.req.OpenAiRequest;
import com.umc_9th.sleepinghero.global.infra.openAi.dto.res.OpenAiResponse;
import com.umc_9th.sleepinghero.global.infra.openAi.prompt.SleepFeedbackSystemPrompt;
import com.umc_9th.sleepinghero.global.infra.openAi.prompt.SleepFeedbackUserPrompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SleepFeedBackServiceImpl implements SleepFeedBackService {

    private final ObjectMapper objectMapper;

    private final OpenAiClient client;
    private final SleepFeedbackSystemPrompt systemPrompt;
    private final SleepFeedbackUserPrompt userPrompt;

    @Override
    public AiSleepFeedBackResponse feedback(
            long sleepDuration, long goalDuration, int sleepStreak, SleepReviewRequest request
    ) {

        AiSleepFeedBackContext context =
                AiSleepFeedBackContext.of(sleepDuration, goalDuration, sleepStreak, request);

        OpenAiRequest openAiRequest =
                OpenAiRequest.from(systemPrompt.value(), userPrompt.build(context));

        OpenAiResponse response = client.chat(openAiRequest);

        try {
            return parseResponse(response.getContent());
        } catch (Exception e) {
            log.warn("AI 응답 파싱 실패, 재요청 시도");
            OpenAiResponse retry = client.chat(openAiRequest);
            return safeParseResponse(retry.getContent());
        }
    }


    // ------------------------------ private ------------------------------


    private AiSleepFeedBackResponse parseResponse(String content) throws JsonProcessingException {
        AiSleepFeedBackResponse feedback = objectMapper.readValue(content, AiSleepFeedBackResponse.class);
        log.info(objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(feedback));
        return feedback;
    }

    private AiSleepFeedBackResponse safeParseResponse(String content) {
        try {
            return parseResponse(content);
        }catch (Exception e) {
            return AiSleepFeedBackResponse.fallBack();
        }
    }
}
