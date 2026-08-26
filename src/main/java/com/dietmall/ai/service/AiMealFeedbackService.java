package com.dietmall.ai.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.dietmall.ai.dto.InitialPlanAiResponse;
import com.dietmall.ai.dto.MealFeedbackAiRequest;
import com.dietmall.ai.dto.MealFeedbackAiResponse;
import com.dietmall.user.entity.CalorieSource;
import com.dietmall.user.entity.MealFood;
import com.dietmall.user.entity.MealRecord;
import com.dietmall.user.repository.MealFoodRepository;
import com.dietmall.user.repository.MealRecordRepository;

@Service
public class AiMealFeedbackService {

    private static final Double
            DEFAULT_DAILY_SODIUM_LIMIT_MG =
                    2000.0;

    private final MealRecordRepository
            mealRecordRepository;

    private final MealFoodRepository
            mealFoodRepository;

    private final AiInitialPlanService
            aiInitialPlanService;

    private final AiIntegrationService
            aiIntegrationService;

    public AiMealFeedbackService(
            MealRecordRepository mealRecordRepository,
            MealFoodRepository mealFoodRepository,
            AiInitialPlanService aiInitialPlanService,
            AiIntegrationService aiIntegrationService
    ) {
        this.mealRecordRepository =
                mealRecordRepository;

        this.mealFoodRepository =
                mealFoodRepository;

        this.aiInitialPlanService =
                aiInitialPlanService;

        this.aiIntegrationService =
                aiIntegrationService;
    }

    public MealFeedbackAiResponse
            createMealFeedback(
                    Long userId,
                    Long mealId
            ) {

        MealRecord mealRecord =
                mealRecordRepository
                        .findByIdAndUserId(
                                mealId,
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "식단 기록을 찾을 수 없습니다."
                                )
                        );

        List<MealFood> mealFoods =
                mealFoodRepository
                        .findAllByMealRecordIdOrderByIdAsc(
                                mealRecord.getId()
                        );

        if (mealFoods.isEmpty()) {
            throw new IllegalArgumentException(
                    "식단에 등록된 음식이 없습니다."
            );
        }

        InitialPlanAiResponse initialPlan =
                getOrCreateInitialPlan(userId);

        Double totalCalories =
                calculateTotalCalories(
                        mealFoods
                );

        boolean estimated =
                isNutritionEstimated(
                        mealFoods,
                        totalCalories
                );

        List<String> foodNames =
                mealFoods.stream()
                        .map(MealFood::getFoodName)
                        .toList();

        MealFeedbackAiRequest
                .MealNutritionSnapshot nutrition =
                new MealFeedbackAiRequest
                        .MealNutritionSnapshot(
                                totalCalories,
                                null,
                                null,
                                null,
                                null,
                                estimated
                        );

        MealFeedbackAiRequest
                .DailyCalorieRange calorieRange =
                new MealFeedbackAiRequest
                        .DailyCalorieRange(
                                initialPlan
                                        .dailyCalorieRange()
                                        .minKcal(),
                                initialPlan
                                        .dailyCalorieRange()
                                        .maxKcal(),
                                initialPlan
                                        .dailyCalorieRange()
                                        .isEstimated()
                        );

        MealFeedbackAiRequest
                .MealFeedbackContext context =
                new MealFeedbackAiRequest
                        .MealFeedbackContext(
                                initialPlan
                                        .goalDirection(),
                                calorieRange,
                                DEFAULT_DAILY_SODIUM_LIMIT_MG,
                                List.of()
                        );

        MealFeedbackAiRequest request =
                new MealFeedbackAiRequest(
                        mealRecord.getId(),
                        mealRecord
                                .getMealType()
                                .name(),
                        foodNames,
                        nutrition,
                        context
                );

        MealFeedbackAiResponse response =
                aiIntegrationService
                        .createMealFeedback(
                                request
                        );

        validateResponse(response);

        return response;
    }

    private InitialPlanAiResponse
            getOrCreateInitialPlan(
                    Long userId
            ) {

        try {
            return aiInitialPlanService
                    .getLatestInitialPlan(
                            userId
                    );

        } catch (IllegalArgumentException exception) {
            return aiInitialPlanService
                    .createAndSaveInitialPlan(
                            userId
                    );
        }
    }

    private Double calculateTotalCalories(
            List<MealFood> mealFoods
    ) {
        boolean hasUnresolvedFood =
                mealFoods.stream()
                        .anyMatch(mealFood ->
                                mealFood.getCalories()
                                        == null
                        );

        if (hasUnresolvedFood) {
            return null;
        }

        int totalCalories =
                mealFoods.stream()
                        .map(MealFood::getCalories)
                        .mapToInt(Integer::intValue)
                        .sum();

        return (double) totalCalories;
    }

    private boolean isNutritionEstimated(
            List<MealFood> mealFoods,
            Double totalCalories
    ) {
        if (totalCalories == null) {
            return true;
        }

        return mealFoods.stream()
                .anyMatch(mealFood ->
                        mealFood.getCalorieSource()
                                == CalorieSource
                                        .AI_ESTIMATED
                );
    }

    private void validateResponse(
            MealFeedbackAiResponse response
    ) {
        Objects.requireNonNull(
                response,
                "AI 식단 피드백 응답이 없습니다."
        );

        Objects.requireNonNull(
                response.version(),
                "AI 응답 버전이 없습니다."
        );

        Objects.requireNonNull(
                response.generatedAt(),
                "AI 피드백 생성 시각이 없습니다."
        );

        Objects.requireNonNull(
                response.status(),
                "AI 피드백 상태가 없습니다."
        );

        Objects.requireNonNull(
                response.summary(),
                "AI 피드백 요약이 없습니다."
        );

        Objects.requireNonNull(
                response.disclaimer(),
                "AI 피드백 안내 문구가 없습니다."
        );
    }
}