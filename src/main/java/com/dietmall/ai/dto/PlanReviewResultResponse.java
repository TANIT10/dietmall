package com.dietmall.ai.dto;

import java.time.Instant;

public record PlanReviewResultResponse(
        Long reviewId,
        Instant effectiveAt,
        PlanReviewAiResponse review
) {
}