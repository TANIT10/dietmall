package com.dietmall.ai.dto;

import java.time.Instant;
import java.util.List;

public record CoachChatResponse(

        String version,

        Instant generatedAt,

        String reply,

        String category,

        String safetyLevel,

        List<String> suggestions,

        List<String> warnings,

        String disclaimer

) {
}