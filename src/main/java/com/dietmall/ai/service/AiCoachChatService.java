package com.dietmall.ai.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.dietmall.ai.dto.CoachChatAiRequest;
import com.dietmall.ai.dto.CoachChatRequest;
import com.dietmall.ai.dto.CoachChatResponse;
import com.dietmall.ai.dto.InitialPlanAiResponse;

@Service
public class AiCoachChatService {

    private final AiInitialPlanService
            aiInitialPlanService;

    private final AiIntegrationService
            aiIntegrationService;

    public AiCoachChatService(
            AiInitialPlanService aiInitialPlanService,
            AiIntegrationService aiIntegrationService
    ) {
        this.aiInitialPlanService =
                aiInitialPlanService;

        this.aiIntegrationService =
                aiIntegrationService;
    }

    public CoachChatResponse createChatResponse(
            Long userId,
            CoachChatRequest request
    ) {
        Objects.requireNonNull(
                request,
                "AI PT 채팅 요청이 없습니다."
        );

        InitialPlanAiResponse initialPlan =
                getOrCreateInitialPlan(userId);

        InitialPlanAiResponse.DailyCalorieRange
                initialCalorieRange =
                initialPlan.dailyCalorieRange();

        CoachChatAiRequest.DailyCalorieRange
                calorieRange =
                new CoachChatAiRequest
                        .DailyCalorieRange(
                        initialCalorieRange.minKcal(),
                        initialCalorieRange.maxKcal(),
                        initialCalorieRange.isEstimated()
                );

        CoachChatAiRequest.CoachContext context =
                new CoachChatAiRequest.CoachContext(
                        initialPlan.goalDirection(),
                        calorieRange,
                        List.of(),
                        List.of()
                );

        List<CoachChatRequest.ConversationMessage>
                conversationHistory =
                request.conversationHistory() == null
                        ? List.of()
                        : request.conversationHistory();

        CoachChatAiRequest aiRequest =
                new CoachChatAiRequest(
                        request.message(),
                        conversationHistory,
                        context
                );

        CoachChatResponse response =
                aiIntegrationService
                        .createCoachChat(aiRequest);

        validateResponse(response);

        return response;
    }

    private InitialPlanAiResponse
            getOrCreateInitialPlan(
                    Long userId
            ) {
        try {
            return aiInitialPlanService
                    .getLatestInitialPlan(userId);

        } catch (IllegalArgumentException exception) {
            return aiInitialPlanService
                    .createAndSaveInitialPlan(userId);
        }
    }

    private void validateResponse(
            CoachChatResponse response
    ) {
        Objects.requireNonNull(
                response,
                "AI PT 채팅 응답이 없습니다."
        );

        Objects.requireNonNull(
                response.version(),
                "AI 응답 버전이 없습니다."
        );

        Objects.requireNonNull(
                response.generatedAt(),
                "AI 응답 생성 시각이 없습니다."
        );

        Objects.requireNonNull(
                response.reply(),
                "AI PT 답변이 없습니다."
        );

        Objects.requireNonNull(
                response.category(),
                "AI PT 답변 분류가 없습니다."
        );

        Objects.requireNonNull(
                response.safetyLevel(),
                "AI PT 안전 등급이 없습니다."
        );

        Objects.requireNonNull(
                response.suggestions(),
                "AI PT 추천 목록이 없습니다."
        );

        Objects.requireNonNull(
                response.warnings(),
                "AI PT 경고 목록이 없습니다."
        );

        Objects.requireNonNull(
                response.disclaimer(),
                "AI PT 안내 문구가 없습니다."
        );
    }
}