package com.dietmall.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.user.dto.OnboardingRequest;
import com.dietmall.user.service.OnboardingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(
            OnboardingService onboardingService) {

        this.onboardingService = onboardingService;
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, String>> completeOnboarding(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid OnboardingRequest request) {

        Long userId = Long.valueOf(jwt.getSubject());

        onboardingService.completeOnboarding(
                userId,
                request
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "온보딩 완료"
                )
        );
    }
}