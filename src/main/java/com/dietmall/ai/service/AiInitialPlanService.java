package com.dietmall.ai.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.ai.dto.InitialPlanAiRequest;
import com.dietmall.ai.dto.InitialPlanAiResponse;
import com.dietmall.ai.entity.AiInitialPlan;
import com.dietmall.ai.repository.AiInitialPlanRepository;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

import tools.jackson.databind.ObjectMapper;

@Service
public class AiInitialPlanService {

    private final UserRepository userRepository;
    private final AiContextService aiContextService;
    private final AiIntegrationService aiIntegrationService;
    private final AiInitialPlanRepository aiInitialPlanRepository;
    private final ObjectMapper objectMapper;

    public AiInitialPlanService(
            UserRepository userRepository,
            AiContextService aiContextService,
            AiIntegrationService aiIntegrationService,
            AiInitialPlanRepository aiInitialPlanRepository,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.aiContextService = aiContextService;
        this.aiIntegrationService = aiIntegrationService;
        this.aiInitialPlanRepository =
                aiInitialPlanRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public InitialPlanAiResponse createAndSaveInitialPlan(
            Long userId
    ) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        InitialPlanAiRequest request =
                aiContextService.buildInitialPlanRequest(
                        userId
                );

        InitialPlanAiResponse response =
                aiIntegrationService.createInitialPlan(
                        request
                );

        validateResponse(response);

        String responseJson = writeResponseJson(response);

        AiInitialPlan initialPlan = new AiInitialPlan(
                user,
                response.version(),
                response.generatedAt(),
                response.summary(),
                response.goalDirection(),
                request.currentWeightKg(),
                response.dailyCalorieRange().minKcal(),
                response.dailyCalorieRange().maxKcal(),
                Boolean.TRUE.equals(
                        response.dailyCalorieRange()
                                .isEstimated()
                ),
                responseJson
        );

        aiInitialPlanRepository.save(initialPlan);

        return response;
    }

    @Transactional(readOnly = true)
    public InitialPlanAiResponse getLatestInitialPlan(
            Long userId
    ) {
        AiInitialPlan initialPlan =
                aiInitialPlanRepository
                        .findTopByUserIdOrderByCreatedAtDesc(
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "저장된 AI 초기 플랜이 없습니다."
                                )
                        );

        return readResponseJson(
                initialPlan.getResponseJson()
        );
    }

    private void validateResponse(
            InitialPlanAiResponse response
    ) {
        Objects.requireNonNull(
                response,
                "AI 초기 플랜 응답이 없습니다."
        );
        Objects.requireNonNull(
                response.version(),
                "AI 응답 버전이 없습니다."
        );
        Objects.requireNonNull(
                response.generatedAt(),
                "AI 플랜 생성 시각이 없습니다."
        );
        Objects.requireNonNull(
                response.summary(),
                "AI 플랜 요약이 없습니다."
        );
        Objects.requireNonNull(
                response.goalDirection(),
                "AI 목표 방향이 없습니다."
        );
        Objects.requireNonNull(
                response.dailyCalorieRange(),
                "AI 권장 칼로리 범위가 없습니다."
        );
        Objects.requireNonNull(
                response.dailyCalorieRange().minKcal(),
                "AI 최소 권장 칼로리가 없습니다."
        );
        Objects.requireNonNull(
                response.dailyCalorieRange().maxKcal(),
                "AI 최대 권장 칼로리가 없습니다."
        );
    }

    private String writeResponseJson(
            InitialPlanAiResponse response
    ) {
        try {
            return objectMapper.writeValueAsString(
                    response
            );

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "AI 초기 플랜 응답을 저장할 수 없습니다.",
                    exception
            );
        }
    }

    private InitialPlanAiResponse readResponseJson(
            String responseJson
    ) {
        try {
            return objectMapper.readValue(
                    responseJson,
                    InitialPlanAiResponse.class
            );

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "저장된 AI 초기 플랜을 읽을 수 없습니다.",
                    exception
            );
        }
    }
}