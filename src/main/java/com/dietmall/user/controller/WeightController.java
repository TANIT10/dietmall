package com.dietmall.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.user.dto.WeightRecordRequest;
import com.dietmall.user.dto.WeightRecordResponse;
import com.dietmall.user.service.WeightService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/weights")
@SecurityRequirement(name = "bearerAuth")
public class WeightController {

    private final WeightService weightService;

    public WeightController(
            WeightService weightService) {

        this.weightService = weightService;
    }

    // 새 체중 기록 추가
    @PostMapping
    public ResponseEntity<WeightRecordResponse> addWeight(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid WeightRecordRequest request) {

        Long userId = Long.valueOf(jwt.getSubject());

        WeightRecordResponse response =
                weightService.addWeight(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // 현재 체중 조회
    @GetMapping("/current")
    public ResponseEntity<WeightRecordResponse> getCurrentWeight(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        WeightRecordResponse response =
                weightService.getCurrentWeight(userId);

        return ResponseEntity.ok(response);
    }

    // 체중 기록 전체 조회
    @GetMapping
    public ResponseEntity<List<WeightRecordResponse>> getWeightHistory(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        List<WeightRecordResponse> response =
                weightService.getWeightHistory(userId);

        return ResponseEntity.ok(response);
    }
}