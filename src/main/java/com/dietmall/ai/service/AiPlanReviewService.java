package com.dietmall.ai.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.ai.dto.PlanReviewAiRequest;
import com.dietmall.ai.dto.PlanReviewAiResponse;
import com.dietmall.ai.dto.PlanReviewResultResponse;
import com.dietmall.ai.entity.AiPlanReview;
import com.dietmall.ai.repository.AiPlanReviewRepository;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

import tools.jackson.databind.ObjectMapper;

@Service
public class AiPlanReviewService {

    private static final ZoneId KOREA_ZONE =
            ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;

    private final PlanReviewContextService
            planReviewContextService;

    private final AiIntegrationService aiIntegrationService;

    private final AiPlanReviewRepository
            aiPlanReviewRepository;

    private final ObjectMapper objectMapper;

    public AiPlanReviewService(
            UserRepository userRepository,
            PlanReviewContextService planReviewContextService,
            AiIntegrationService aiIntegrationService,
            AiPlanReviewRepository aiPlanReviewRepository,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.planReviewContextService =
                planReviewContextService;
        this.aiIntegrationService = aiIntegrationService;
        this.aiPlanReviewRepository =
                aiPlanReviewRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public PlanReviewResultResponse
            createAndSaveRecentReview(
                    Long userId
            ) {
        LocalDate reviewEndDate =
                LocalDate.now(KOREA_ZONE);

        LocalDate reviewStartDate =
                reviewEndDate.minusDays(13);

        PlanReviewAiRequest request =
                planReviewContextService
                        .buildReviewRequest(
                                userId,
                                reviewStartDate,
                                reviewEndDate
                        );

        return createAndSaveReview(
                userId,
                request
        );
    }

    private PlanReviewResultResponse createAndSaveReview(
            Long userId,
            PlanReviewAiRequest request
    ) {
        validateRequest(request);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        PlanReviewAiResponse response =
                aiIntegrationService.reviewPlan(request);

        validateResponse(response);

        String responseJson = writeResponseJson(response);

        PlanReviewAiResponse.PlanAdjustment adjustment =
                response.adjustment();

        PlanReviewAiResponse
                .RecommendedDailyCalorieRange calorieRange =
                adjustment.recommendedDailyCalorieRange();

        AiPlanReview planReview = new AiPlanReview(
                user,
                request.reviewStartDate(),
                request.reviewEndDate(),
                response.version(),
                response.generatedAt(),
                response.status(),
                response.summary(),
                Boolean.TRUE.equals(
                        adjustment.adjustmentNeeded()
                ),
                calorieRange == null
                        ? null
                        : calorieRange.minKcal(),
                calorieRange == null
                        ? null
                        : calorieRange.maxKcal(),
                adjustment
                        .recommendedWorkoutDaysPerWeek(),
                adjustment
                        .recommendedWorkoutMinutesPerDay(),
                responseJson
        );

        AiPlanReview savedReview =
                aiPlanReviewRepository.save(planReview);

        return new PlanReviewResultResponse(
                savedReview.getId(),
                savedReview.getEffectiveAt(),
                response
        );
    }

    @Transactional(readOnly = true)
    public PlanReviewResultResponse getLatestReview(
            Long userId
    ) {
        AiPlanReview planReview =
                aiPlanReviewRepository
                        .findTopByUserIdOrderByCreatedAtDesc(
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "저장된 AI 플랜 재분석 결과가 없습니다."
                                )
                        );

        return toResultResponse(planReview);
    }

    @Transactional
    public PlanReviewResultResponse applyAdjustment(
            Long userId,
            Long reviewId
    ) {
        AiPlanReview planReview =
                aiPlanReviewRepository
                        .findByIdAndUserId(
                                reviewId,
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "AI 플랜 재분석 결과를 찾을 수 없습니다."
                                )
                        );

        planReview.applyAdjustment(Instant.now());

        return toResultResponse(planReview);
    }

    private PlanReviewResultResponse toResultResponse(
            AiPlanReview planReview
    ) {
        PlanReviewAiResponse response =
                readResponseJson(
                        planReview.getResponseJson()
                );

        return new PlanReviewResultResponse(
                planReview.getId(),
                planReview.getEffectiveAt(),
                response
        );
    }

    private void validateRequest(
            PlanReviewAiRequest request
    ) {
        Objects.requireNonNull(
                request,
                "플랜 재분석 요청이 없습니다."
        );
        Objects.requireNonNull(
                request.reviewStartDate(),
                "재분석 시작일이 없습니다."
        );
        Objects.requireNonNull(
                request.reviewEndDate(),
                "재분석 종료일이 없습니다."
        );
        Objects.requireNonNull(
                request.currentPlan(),
                "현재 플랜 정보가 없습니다."
        );

        if (
                request.reviewEndDate()
                        .isBefore(request.reviewStartDate())
        ) {
            throw new IllegalArgumentException(
                    "재분석 종료일은 시작일보다 빠를 수 없습니다."
            );
        }
    }

    private void validateResponse(
            PlanReviewAiResponse response
    ) {
        Objects.requireNonNull(
                response,
                "AI 플랜 재분석 응답이 없습니다."
        );
        Objects.requireNonNull(
                response.version(),
                "AI 응답 버전이 없습니다."
        );
        Objects.requireNonNull(
                response.generatedAt(),
                "AI 재분석 생성 시각이 없습니다."
        );
        Objects.requireNonNull(
                response.status(),
                "AI 재분석 상태가 없습니다."
        );
        Objects.requireNonNull(
                response.summary(),
                "AI 재분석 요약이 없습니다."
        );
        Objects.requireNonNull(
                response.adjustment(),
                "AI 플랜 조정 정보가 없습니다."
        );
        Objects.requireNonNull(
                response.adjustment().adjustmentNeeded(),
                "AI 플랜 조정 필요 여부가 없습니다."
        );
    }

    private String writeResponseJson(
            PlanReviewAiResponse response
    ) {
        try {
            return objectMapper.writeValueAsString(
                    response
            );

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "AI 플랜 재분석 결과를 저장할 수 없습니다.",
                    exception
            );
        }
    }

    private PlanReviewAiResponse readResponseJson(
            String responseJson
    ) {
        try {
            return objectMapper.readValue(
                    responseJson,
                    PlanReviewAiResponse.class
            );

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "저장된 AI 플랜 재분석 결과를 읽을 수 없습니다.",
                    exception
            );
        }
    }
}