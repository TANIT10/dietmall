package com.dietmall.ai.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PlanReviewAiRequest(
        LocalDate reviewStartDate,
        LocalDate reviewEndDate,
        CurrentPlan currentPlan,
        List<DailyRecord> records
) {

    public PlanReviewAiRequest {
        records = records == null
                ? List.of()
                : List.copyOf(records);
    }

    public record CurrentPlan(
            String goalDirection,
            BigDecimal startingWeightKg,
            BigDecimal targetWeightKg,
            DailyCalorieRange dailyCalorieRange,
            Integer workoutDaysPerWeek,
            Integer workoutMinutesPerDay
    ) {
    }

    public record DailyCalorieRange(
            Integer minKcal,
            Integer maxKcal,
            Boolean isEstimated
    ) {
    }

    public record DailyRecord(
            LocalDate recordDate,
            BigDecimal weightKg,
            BigDecimal caloriesKcal,
            BigDecimal proteinG,
            Integer workoutMinutes
    ) {
    }
}