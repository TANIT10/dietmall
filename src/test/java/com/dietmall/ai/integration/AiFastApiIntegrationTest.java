package com.dietmall.ai.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.dietmall.ai.dto.InitialPlanAiRequest;
import com.dietmall.ai.dto.InitialPlanAiResponse;
import com.dietmall.ai.dto.PlanReviewAiRequest;
import com.dietmall.ai.dto.PlanReviewAiResponse;
import com.dietmall.ai.service.AiIntegrationService;
import com.google.firebase.FirebaseApp;

@SpringBootTest
@ActiveProfiles("test")
@EnabledIfSystemProperty(
        named = "ai.integration",
        matches = "true"
)
class AiFastApiIntegrationTest {

    @MockitoBean
    private FirebaseApp firebaseApp;

    @Autowired
    private AiIntegrationService aiIntegrationService;

    @Test
    void callsRealFastApiSuccessfully() {
        assertTrue(
                aiIntegrationService
                        .getHealth()
                        .isHealthy()
        );

        InitialPlanAiRequest initialRequest =
                new InitialPlanAiRequest(
                        "MALE",
                        30,
                        new BigDecimal("175.00"),
                        new BigDecimal("85.00"),
                        new BigDecimal("75.00"),
                        20,
                        "ACTIVE",
                        4,
                        45,
                        List.of(),
                        List.of()
                );

        InitialPlanAiResponse initialResponse =
                aiIntegrationService.createInitialPlan(
                        initialRequest
                );

        assertNotNull(initialResponse);
        assertNotNull(initialResponse.summary());
        assertNotNull(
                initialResponse.dailyCalorieRange()
        );

        LocalDate reviewStartDate =
                LocalDate.of(2026, 8, 1);

        List<PlanReviewAiRequest.DailyRecord>
                dailyRecords = List.of(
                new PlanReviewAiRequest.DailyRecord(
                        reviewStartDate,
                        new BigDecimal("85.00"),
                        new BigDecimal("2400"),
                        null,
                        45
                ),
                new PlanReviewAiRequest.DailyRecord(
                        reviewStartDate.plusDays(1),
                        null,
                        new BigDecimal("2350"),
                        null,
                        0
                ),
                new PlanReviewAiRequest.DailyRecord(
                        reviewStartDate.plusDays(2),
                        null,
                        new BigDecimal("2450"),
                        null,
                        45
                ),
                new PlanReviewAiRequest.DailyRecord(
                        reviewStartDate.plusDays(3),
                        null,
                        new BigDecimal("2300"),
                        null,
                        0
                ),
                new PlanReviewAiRequest.DailyRecord(
                        reviewStartDate.plusDays(4),
                        null,
                        new BigDecimal("2500"),
                        null,
                        45
                ),
                new PlanReviewAiRequest.DailyRecord(
                        reviewStartDate.plusDays(5),
                        null,
                        new BigDecimal("2380"),
                        null,
                        0
                ),
                new PlanReviewAiRequest.DailyRecord(
                        reviewStartDate.plusDays(6),
                        new BigDecimal("84.50"),
                        new BigDecimal("2420"),
                        null,
                        45
                )
        );

        PlanReviewAiRequest reviewRequest =
                new PlanReviewAiRequest(
                        reviewStartDate,
                        reviewStartDate.plusDays(6),
                        new PlanReviewAiRequest.CurrentPlan(
                                "WEIGHT_LOSS",
                                new BigDecimal("85.00"),
                                new BigDecimal("75.00"),
                                new PlanReviewAiRequest
                                        .DailyCalorieRange(
                                        initialResponse
                                                .dailyCalorieRange()
                                                .minKcal(),
                                        initialResponse
                                                .dailyCalorieRange()
                                                .maxKcal(),
                                        initialResponse
                                                .dailyCalorieRange()
                                                .isEstimated()
                                ),
                                4,
                                45
                        ),
                        dailyRecords
                );

        PlanReviewAiResponse reviewResponse =
                aiIntegrationService.reviewPlan(
                        reviewRequest
                );

        assertNotNull(reviewResponse);
        assertNotNull(reviewResponse.status());
        assertNotNull(reviewResponse.summary());
        assertNotNull(reviewResponse.adjustment());
    }
}