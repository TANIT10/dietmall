package com.dietmall.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.ai.dto.InitialPlanAiResponse;
import com.dietmall.ai.service.AiInitialPlanService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/ai/plans")
public class AiInitialPlanController {

    private final AiInitialPlanService aiInitialPlanService;

    public AiInitialPlanController(
            AiInitialPlanService aiInitialPlanService
    ) {
        this.aiInitialPlanService =
                aiInitialPlanService;
    }

    @PostMapping("/initial")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<InitialPlanAiResponse>
            createInitialPlan(
                    @AuthenticationPrincipal Jwt jwt
            ) {
        Long userId = Long.valueOf(jwt.getSubject());

        InitialPlanAiResponse response =
                aiInitialPlanService
                        .createAndSaveInitialPlan(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/initial/latest")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<InitialPlanAiResponse>
            getLatestInitialPlan(
                    @AuthenticationPrincipal Jwt jwt
            ) {
        Long userId = Long.valueOf(jwt.getSubject());

        InitialPlanAiResponse response =
                aiInitialPlanService
                        .getLatestInitialPlan(userId);

        return ResponseEntity.ok(response);
    }
}