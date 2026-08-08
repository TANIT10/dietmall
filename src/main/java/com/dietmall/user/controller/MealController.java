package com.dietmall.user.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietmall.user.dto.MealRecordRequest;
import com.dietmall.user.dto.MealRecordResponse;
import com.dietmall.user.dto.MealTodaySummaryResponse;
import com.dietmall.user.service.MealService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/meals")
@SecurityRequirement(name = "bearerAuth")
public class MealController {

    private final MealService mealService;

    public MealController(
            MealService mealService) {

        this.mealService = mealService;
    }

    // 식단 기록 추가
    @PostMapping
    public ResponseEntity<MealRecordResponse> addMeal(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid MealRecordRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        MealRecordResponse response =
                mealService.addMeal(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // 전체 식단 기록 조회
    @GetMapping
    public ResponseEntity<List<MealRecordResponse>> getAllMeals(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        return ResponseEntity.ok(
                mealService.getAllMeals(
                        userId
                )
        );
    }

    // 오늘 식단 조회
    @GetMapping("/today")
    public ResponseEntity<List<MealRecordResponse>> getTodayMeals(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        return ResponseEntity.ok(
                mealService.getTodayMeals(
                        userId
                )
        );
    }

    // 오늘 식단 요약
    @GetMapping("/today/summary")
    public ResponseEntity<MealTodaySummaryResponse> getTodaySummary(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        return ResponseEntity.ok(
                mealService.getTodaySummary(
                        userId
                )
        );
    }

    // 특정 날짜 식단 조회
    @GetMapping("/date")
    public ResponseEntity<List<MealRecordResponse>> getMealsByDate(
            @AuthenticationPrincipal Jwt jwt,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate date) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        return ResponseEntity.ok(
                mealService.getMealsByDate(
                        userId,
                        date
                )
        );
    }
}