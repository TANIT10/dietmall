package com.dietmall.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.user.dto.GoalResponse;
import com.dietmall.user.dto.GoalUpdateRequest;
import com.dietmall.user.service.GoalService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/goals")
@SecurityRequirement(name = "bearerAuth")
public class GoalController {

    private final GoalService goalService;

    public GoalController(
            GoalService goalService) {

        this.goalService = goalService;
    }

    // 현재 목표 체중 조회
    @GetMapping
    public ResponseEntity<GoalResponse> getGoal(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        GoalResponse response =
                goalService.getGoal(userId);

        return ResponseEntity.ok(response);
    }

    // 목표 체중 수정
    @PatchMapping
    public ResponseEntity<GoalResponse> updateGoal(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid GoalUpdateRequest request) {

        Long userId = Long.valueOf(jwt.getSubject());

        GoalResponse response =
                goalService.updateGoal(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }
}