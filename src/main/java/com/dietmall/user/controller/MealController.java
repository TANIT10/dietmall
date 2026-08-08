package com.dietmall.user.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    // 식단 사진 업로드
    @PostMapping(
            value = "/{mealId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<MealRecordResponse> uploadMealImage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long mealId,
            @RequestPart("file") MultipartFile file) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        MealRecordResponse response =
                mealService.uploadMealImage(
                        userId,
                        mealId,
                        file
                );

        return ResponseEntity.ok(response);
    }

    // 식단 사진 조회
    @GetMapping("/{mealId}/image")
    public ResponseEntity<Resource> getMealImage(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long mealId) {

        Long userId =
                Long.valueOf(jwt.getSubject());

        Resource resource =
                mealService.getMealImage(
                        userId,
                        mealId
                );

        MediaType mediaType =
                getImageMediaType(
                        resource.getFilename()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\""
                                + resource.getFilename()
                                + "\""
                )
                .contentType(mediaType)
                .body(resource);
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

    // 이미지 확장자에 맞는 Content-Type 결정
    private MediaType getImageMediaType(
            String filename) {

        if (filename == null) {

            return MediaType.APPLICATION_OCTET_STREAM;
        }

        String lowerFilename =
                filename.toLowerCase();

        if (lowerFilename.endsWith(".jpg")
                || lowerFilename.endsWith(".jpeg")) {

            return MediaType.IMAGE_JPEG;
        }

        if (lowerFilename.endsWith(".png")) {

            return MediaType.IMAGE_PNG;
        }

        if (lowerFilename.endsWith(".webp")) {

            return MediaType.parseMediaType(
                    "image/webp"
            );
        }

        if (lowerFilename.endsWith(".heic")) {

            return MediaType.parseMediaType(
                    "image/heic"
            );
        }

        if (lowerFilename.endsWith(".heif")) {

            return MediaType.parseMediaType(
                    "image/heif"
            );
        }

        return MediaType.APPLICATION_OCTET_STREAM;
    }
}