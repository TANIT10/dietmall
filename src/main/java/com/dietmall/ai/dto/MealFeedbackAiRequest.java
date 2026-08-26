package com.dietmall.ai.dto;

import java.util.List;

public record MealFeedbackAiRequest(
        Long mealRecordId,
        String mealType,
        List<String> foodNames,
        MealNutritionSnapshot nutrition,
        MealFeedbackContext context
) {

    public MealFeedbackAiRequest {
        foodNames =
                foodNames == null
                        ? List.of()
                        : List.copyOf(foodNames);
    }

    public record MealNutritionSnapshot(
            Double caloriesKcal,
            Double carbohydrateG,
            Double proteinG,
            Double fatG,
            Double sodiumMg,
            Boolean isEstimated
    ) {
    }

    public record MealFeedbackContext(
            String goalDirection,
            DailyCalorieRange dailyCalorieRange,
            Double dailySodiumLimitMg,
            List<String> dietaryRestrictions
    ) {

        public MealFeedbackContext {
            dietaryRestrictions =
                    dietaryRestrictions == null
                            ? List.of()
                            : List.copyOf(
                                    dietaryRestrictions
                            );
        }
    }

    public record DailyCalorieRange(
            Integer minKcal,
            Integer maxKcal,
            Boolean isEstimated
    ) {
    }
}