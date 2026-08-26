package com.dietmall.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.ai.dto.MealFeedbackAiResponse;
import com.dietmall.ai.service.AiMealFeedbackService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/ai/meals")
@SecurityRequirement(name = "bearerAuth")
public class AiMealFeedbackController {

    private final AiMealFeedbackService
            aiMealFeedbackService;

    public AiMealFeedbackController(
            AiMealFeedbackService
                    aiMealFeedbackService
    ) {
        this.aiMealFeedbackService =
                aiMealFeedbackService;
    }

    @PostMapping("/{mealId}/feedback")
    public ResponseEntity<MealFeedbackAiResponse>
            createMealFeedback(
                    @AuthenticationPrincipal Jwt jwt,
                    @PathVariable Long mealId
            ) {

        Long userId =
                Long.valueOf(
                        jwt.getSubject()
                );

        MealFeedbackAiResponse response =
                aiMealFeedbackService
                        .createMealFeedback(
                                userId,
                                mealId
                        );

        return ResponseEntity.ok(response);
    }
}