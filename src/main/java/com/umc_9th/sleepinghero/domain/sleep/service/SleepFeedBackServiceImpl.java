package com.umc_9th.sleepinghero.domain.sleep.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBackContext;
import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.converter.SleepConverter;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepFeedBack;
import com.umc_9th.sleepinghero.domain.sleep.entity.SleepReview;
import com.umc_9th.sleepinghero.domain.sleep.exception.SleepErrorCode;
import com.umc_9th.sleepinghero.domain.sleep.repository.SleepFeedBackRepository;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import com.umc_9th.sleepinghero.global.infra.openAi.OpenAiClient;
import com.umc_9th.sleepinghero.global.infra.openAi.dto.req.OpenAiRequest;
import com.umc_9th.sleepinghero.global.infra.openAi.dto.res.OpenAiResponse;
import com.umc_9th.sleepinghero.global.infra.openAi.prompt.SleepFeedbackSystemPrompt;
import com.umc_9th.sleepinghero.global.infra.openAi.prompt.SleepFeedbackUserPrompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SleepFeedBackServiceImpl implements SleepFeedBackService {

    private final SleepFeedBackRepository sleepFeedBackRepository;

    private final ObjectMapper objectMapper;

    private final OpenAiClient client;
    private final SleepFeedbackSystemPrompt systemPrompt;
    private final SleepFeedbackUserPrompt userPrompt;

    @Override
    @Transactional
    public AiSleepFeedBack feedback(
            long sleepDuration,
            long goalDuration,
            int sleepStreak,
            SleepReview review
    ) {

        validateSleepFeedBack(review);

        OpenAiRequest openAiRequest = generateAiRequest(
                sleepDuration, goalDuration, sleepStreak, review
        );

        try {
            return generateSleepFeedBack(openAiRequest, review);

        } catch (Exception e) {
            return fallBack(review, e);
        }
    }

    // ---------------- private ----------------


    // 파싱

    private AiSleepFeedBack parseResponse(String content)
            throws JsonProcessingException {

        AiSleepFeedBack response =
                objectMapper.readValue(content, AiSleepFeedBack.class);

        log.info("AI Feedback :\n{}",
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(response)
        );

        return response;
    }


    // 검증

    private void validateSleepFeedBack(SleepReview review){
        if (sleepFeedBackRepository.existsBySleepReview(review)) {
            throw new GeneralException(SleepErrorCode.SLEEP_FEEDBACK_ALREADY_EXISTS);
        }
    }


    // 생성

    private OpenAiRequest generateAiRequest(
            long sleepDuration,
            long goalDuration,
            int sleepStreak,
            SleepReview review
    ) {
        AiSleepFeedBackContext context =
                AiSleepFeedBackContext.of(
                        sleepDuration,
                        goalDuration,
                        sleepStreak,
                        review.getStar(),
                        review.getComment()
                );

        return OpenAiRequest.from(
                        systemPrompt.value(),
                        userPrompt.build(context)
                );
    }

    private AiSleepFeedBack generateSleepFeedBack(
            OpenAiRequest openAiRequest, SleepReview review
    ) throws JsonProcessingException
    {
        OpenAiResponse aiResponse = client.chat(openAiRequest);

        AiSleepFeedBack feedBack = parseResponse(aiResponse.getContent());

        saveFeedBack(feedBack, review);

        return feedBack;
    }


    // 폴백

    private AiSleepFeedBack fallBack(SleepReview review, Exception e){
        log.warn("AI 응답 실패 → fallback 저장", e);

        AiSleepFeedBack fallback =
                AiSleepFeedBack.fallBack();

        saveFeedBack(fallback, review);

        return fallback;
    }


    // 저장

    private void saveFeedBack(AiSleepFeedBack feedBack, SleepReview review) {
        sleepFeedBackRepository.save(SleepFeedBack.builder()
                .summary(feedBack.summary())
                .positives(feedBack.positives())
                .improvements(feedBack.improvements())
                .cheering(feedBack.cheering())
                .sleepReview(review)
                .build());
    }
}
