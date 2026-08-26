package com.dietmall.ai.dto;

import java.time.Instant;
import java.util.List;

public record MealFeedbackAiResponse(
        String version,
        Instant generatedAt,
        Long mealRecordId,
        String status,
        String summary,
        List<MealFeedbackItem> feedback,
        List<String> nextMealSuggestions,
        List<String> warnings,
        String disclaimer
) {

    public MealFeedbackAiResponse {
        feedback =
                feedback == null
                        ? List.of()
                        : List.copyOf(feedback);

        nextMealSuggestions =
                nextMealSuggestions == null
                        ? List.of()
                        : List.copyOf(
                                nextMealSuggestions
                        );

        warnings =
                warnings == null
                        ? List.of()
                        : List.copyOf(warnings);
    }

    public record MealFeedbackItem(
            String code,
            String level,
            String message
    ) {
    }
}