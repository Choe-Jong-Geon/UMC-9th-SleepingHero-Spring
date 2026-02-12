package com.umc_9th.sleepinghero.global.infra.openAi.prompt;

import com.umc_9th.sleepinghero.domain.sleep.ai.AiSleepFeedBackContext;
import org.springframework.stereotype.Component;

@Component
public class SleepFeedbackUserPrompt {

    public String build(AiSleepFeedBackContext req) {
        return """
        다음은 한 용사의 최근 휴식 기록이다.
        이 정보를 바탕으로 용사의 상태를 평가하라.

        [용사의 휴식 현황]
        - 총 수면 시간: %d분
        - 목표 수면 시간: %d분
        - 연속 수면 달성일: %d일
        - 자기 평가 별점: %d점

        [용사의 한마디]
        "%s"

        위 정보를 기반으로
        판타지 세계관의 스승처럼 용사에게 조언하라.
        """.formatted(
                req.sleepDuration(),
                req.goalDuration(),
                req.sleepStreak(),
                req.star(),
                req.comment()
        );
    }
}
