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

import com.dietmall.user.dto.ExerciseRecordRequest;
import com.dietmall.user.dto.ExerciseRecordResponse;
import com.dietmall.user.dto.ExerciseTodaySummaryResponse;
import com.dietmall.user.service.ExerciseService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exercises")
@SecurityRequirement(name = "bearerAuth")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(
            ExerciseService exerciseService) {

        this.exerciseService = exerciseService;
    }

    // 운동 기록 추가
    @PostMapping
    public ResponseEntity<ExerciseRecordResponse> addExercise(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid ExerciseRecordRequest request) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        ExerciseRecordResponse response =
                exerciseService.addExercise(
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // 전체 운동 기록 조회
    @GetMapping
    public ResponseEntity<List<ExerciseRecordResponse>> getAllExercises(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        return ResponseEntity.ok(
                exerciseService.getAllExercises(
                        userId
                )
        );
    }

    // 오늘 운동 기록 조회
    @GetMapping("/today")
    public ResponseEntity<List<ExerciseRecordResponse>> getTodayExercises(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        return ResponseEntity.ok(
                exerciseService.getTodayExercises(
                        userId
                )
        );
    }

    // 오늘 운동 요약
    @GetMapping("/today/summary")
    public ResponseEntity<ExerciseTodaySummaryResponse> getTodaySummary(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        return ResponseEntity.ok(
                exerciseService.getTodaySummary(
                        userId
                )
        );
    }

    // 특정 날짜 운동 기록 조회
    @GetMapping("/date")
    public ResponseEntity<List<ExerciseRecordResponse>> getExercisesByDate(
            @AuthenticationPrincipal Jwt jwt,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate date) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        return ResponseEntity.ok(
                exerciseService.getExercisesByDate(
                        userId,
                        date
                )
        );
    }
}