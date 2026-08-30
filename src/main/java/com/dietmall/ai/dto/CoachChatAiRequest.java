package com.dietmall.ai.dto;

import java.util.List;

public record CoachChatAiRequest(

        String message,

        List<CoachChatRequest.ConversationMessage>
                conversationHistory,

        CoachContext context

) {

    public record CoachContext(

            String goalDirection,

            DailyCalorieRange dailyCalorieRange,

            List<String> dietaryRestrictions,

            List<String> healthNotes

    ) {
    }

    public record DailyCalorieRange(

            Integer minKcal,

            Integer maxKcal,

            Boolean isEstimated

    ) {
    }
}