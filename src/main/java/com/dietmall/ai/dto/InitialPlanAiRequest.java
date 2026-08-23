package com.dietmall.ai.dto;

import java.math.BigDecimal;
import java.util.List;


public record InitialPlanAiRequest(
        String gender,
        Integer age,
        BigDecimal heightCm,
        BigDecimal currentWeightKg,
        BigDecimal targetWeightKg,
        Integer goalDurationWeeks,
        String activityLevel,
        Integer workoutDaysPerWeek,
        Integer workoutMinutesPerDay,
        List<String> dietaryRestrictions,
        List<String> healthNotes
) {
    public InitialPlanAiRequest {
        dietaryRestrictions =
                dietaryRestrictions == null
                        ? List.of()
                        : List.copyOf(dietaryRestrictions);

        healthNotes =
                healthNotes == null
                        ? List.of()
                        : List.copyOf(healthNotes);
    }
}