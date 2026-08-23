package com.dietmall.ai.dto;

import java.time.Instant;
import java.util.List;

public record InitialPlanAiResponse(
        String version,
        Instant generatedAt,
        String summary,
        String goalDirection,
        CalorieCalculation calorieCalculation,
        DailyCalorieRange dailyCalorieRange,
        MealGuide mealGuide,
        ExerciseGuide exerciseGuide,
        NumberRange expectedWeeklyChangeKg,
        List<String> warnings,
        String disclaimer
) {

    public record CalorieCalculation(
            String formula,
            Double bmrKcal,
            Double maintenanceKcal,
            Double activityFactor,
            Integer minimumDailyCalories,
            Boolean isEstimated
    ) {
    }

    public record DailyCalorieRange(
            Integer minKcal,
            Integer maxKcal,
            Boolean isEstimated
    ) {
    }

    public record MealGuide(
            String summary,
            List<String> principles,
            List<String> recommendedFoods,
            List<String> foodsToLimit
    ) {
    }

    public record ExerciseGuide(
            String summary,
            String weeklyFrequency,
            String sessionMinutes,
            List<String> cardio,
            List<String> strength,
            List<String> cautions
    ) {
    }

    public record NumberRange(
            Double min,
            Double max
    ) {
    }
}