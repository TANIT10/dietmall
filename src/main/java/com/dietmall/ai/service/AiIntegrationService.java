package com.dietmall.ai.service;

import org.springframework.stereotype.Service;

import com.dietmall.ai.client.AiHealthResponse;
import com.dietmall.ai.client.AiServiceClient;
import com.dietmall.ai.client.AiServiceException;
import com.dietmall.ai.dto.InitialPlanAiRequest;
import com.dietmall.ai.dto.InitialPlanAiResponse;
import com.dietmall.ai.dto.MealFeedbackAiRequest;
import com.dietmall.ai.dto.MealFeedbackAiResponse;
import com.dietmall.ai.dto.PlanReviewAiRequest;
import com.dietmall.ai.dto.PlanReviewAiResponse;

@Service
public class AiIntegrationService {

    private static final String HEALTH_PATH =
            "/health";

    private static final String INITIAL_PLAN_PATH =
            "/v1/plans/initial";

    private static final String PLAN_REVIEW_PATH =
            "/v1/plans/review";

    private static final String MEAL_FEEDBACK_PATH =
            "/v1/coach/meal-feedback";

    private final AiServiceClient aiServiceClient;

    public AiIntegrationService(
            AiServiceClient aiServiceClient
    ) {
        this.aiServiceClient =
                aiServiceClient;
    }

    public AiHealthResponse getHealth() {
        return aiServiceClient.get(
                HEALTH_PATH,
                AiHealthResponse.class
        );
    }

    public InitialPlanAiResponse createInitialPlan(
            InitialPlanAiRequest request
    ) {
        return aiServiceClient.post(
                INITIAL_PLAN_PATH,
                request,
                InitialPlanAiResponse.class
        );
    }

    public PlanReviewAiResponse reviewPlan(
            PlanReviewAiRequest request
    ) {
        return aiServiceClient.post(
                PLAN_REVIEW_PATH,
                request,
                PlanReviewAiResponse.class
        );
    }

    public MealFeedbackAiResponse createMealFeedback(
            MealFeedbackAiRequest request
    ) {
        return aiServiceClient.post(
                MEAL_FEEDBACK_PATH,
                request,
                MealFeedbackAiResponse.class
        );
    }

    public boolean isAvailable() {
        try {
            return getHealth().isHealthy();

        } catch (AiServiceException exception) {
            return false;
        }
    }
}