package com.dietmall.ai.dto;

import java.time.Instant;
import java.util.List;

public record PlanReviewAiResponse(
        String version,
        Instant generatedAt,
        String status,
        String summary,
        ReviewMetrics metrics,
        List<String> observations,
        PlanAdjustment adjustment,
        List<String> warnings,
        String disclaimer
) {

    public record ReviewMetrics(
            Integer daysAnalyzed,
            Integer mealRecordDays,
            Integer weightRecordDays,
            Integer workoutDays,
            Double averageDailyCaloriesKcal,
            Double averageDailyProteinG,
            Integer totalWorkoutMinutes,
            Double weightChangeKg
    ) {
    }

    public record PlanAdjustment(
            Boolean adjustmentNeeded,
            RecommendedDailyCalorieRange
                    recommendedDailyCalorieRange,
            Integer recommendedWorkoutDaysPerWeek,
            Integer recommendedWorkoutMinutesPerDay,
            List<String> reasons
    ) {
    }

    public record RecommendedDailyCalorieRange(
            Integer minKcal,
            Integer maxKcal,
            Boolean isEstimated
    ) {
    }
}