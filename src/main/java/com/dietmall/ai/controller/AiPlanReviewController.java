package com.dietmall.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.ai.dto.PlanReviewResultResponse;
import com.dietmall.ai.service.AiPlanReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/ai/plans/reviews")
public class AiPlanReviewController {

    private final AiPlanReviewService aiPlanReviewService;

    public AiPlanReviewController(
            AiPlanReviewService aiPlanReviewService
    ) {
        this.aiPlanReviewService =
                aiPlanReviewService;
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PlanReviewResultResponse>
            createRecentReview(
                    @AuthenticationPrincipal Jwt jwt
            ) {
        Long userId = Long.valueOf(jwt.getSubject());

        PlanReviewResultResponse response =
                aiPlanReviewService
                        .createAndSaveRecentReview(
                                userId
                        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PlanReviewResultResponse>
            getLatestReview(
                    @AuthenticationPrincipal Jwt jwt
            ) {
        Long userId = Long.valueOf(jwt.getSubject());

        PlanReviewResultResponse response =
                aiPlanReviewService.getLatestReview(
                        userId
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reviewId}/apply")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PlanReviewResultResponse>
            applyAdjustment(
                    @AuthenticationPrincipal Jwt jwt,
                    @PathVariable Long reviewId
            ) {
        Long userId = Long.valueOf(jwt.getSubject());

        PlanReviewResultResponse response =
                aiPlanReviewService.applyAdjustment(
                        userId,
                        reviewId
                );

        return ResponseEntity.ok(response);
    }
}