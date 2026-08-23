package com.dietmall.ai.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.ai.dto.PlanReviewAiRequest;
import com.dietmall.ai.entity.AiInitialPlan;
import com.dietmall.ai.entity.AiPlanReview;
import com.dietmall.ai.repository.AiInitialPlanRepository;
import com.dietmall.ai.repository.AiPlanReviewRepository;
import com.dietmall.user.entity.ExerciseRecord;
import com.dietmall.user.entity.Goal;
import com.dietmall.user.entity.MealFood;
import com.dietmall.user.entity.MealRecord;
import com.dietmall.user.entity.UserProfile;
import com.dietmall.user.entity.WeightRecord;
import com.dietmall.user.repository.ExerciseRecordRepository;
import com.dietmall.user.repository.GoalRepository;
import com.dietmall.user.repository.MealFoodRepository;
import com.dietmall.user.repository.MealRecordRepository;
import com.dietmall.user.repository.UserProfileRepository;
import com.dietmall.user.repository.WeightRecordRepository;

@Service
public class PlanReviewContextService {

    private static final ZoneId KOREA_ZONE =
            ZoneId.of("Asia/Seoul");

    private static final long MAX_REVIEW_DAYS = 14;

    private final AiInitialPlanRepository
            aiInitialPlanRepository;

    private final AiPlanReviewRepository
            aiPlanReviewRepository;

    private final UserProfileRepository
            userProfileRepository;

    private final GoalRepository goalRepository;

    private final MealRecordRepository
            mealRecordRepository;

    private final MealFoodRepository
            mealFoodRepository;

    private final ExerciseRecordRepository
            exerciseRecordRepository;

    private final WeightRecordRepository
            weightRecordRepository;

    public PlanReviewContextService(
            AiInitialPlanRepository aiInitialPlanRepository,
            AiPlanReviewRepository aiPlanReviewRepository,
            UserProfileRepository userProfileRepository,
            GoalRepository goalRepository,
            MealRecordRepository mealRecordRepository,
            MealFoodRepository mealFoodRepository,
            ExerciseRecordRepository exerciseRecordRepository,
            WeightRecordRepository weightRecordRepository
    ) {
        this.aiInitialPlanRepository =
                aiInitialPlanRepository;
        this.aiPlanReviewRepository =
                aiPlanReviewRepository;
        this.userProfileRepository =
                userProfileRepository;
        this.goalRepository = goalRepository;
        this.mealRecordRepository =
                mealRecordRepository;
        this.mealFoodRepository =
                mealFoodRepository;
        this.exerciseRecordRepository =
                exerciseRecordRepository;
        this.weightRecordRepository =
                weightRecordRepository;
    }

    @Transactional(readOnly = true)
    public PlanReviewAiRequest buildReviewRequest(
            Long userId,
            LocalDate reviewStartDate,
            LocalDate reviewEndDate
    ) {
        validateReviewPeriod(
                reviewStartDate,
                reviewEndDate
        );

        AiInitialPlan initialPlan =
                aiInitialPlanRepository
                        .findTopByUserIdOrderByCreatedAtDesc(
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "저장된 AI 초기 플랜이 없습니다."
                                )
                        );

        UserProfile userProfile =
                userProfileRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "사용자 프로필을 찾을 수 없습니다."
                                )
                        );

        Goal goal = goalRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자 목표를 찾을 수 없습니다."
                        )
                );

        Optional<AiPlanReview> appliedReview =
                aiPlanReviewRepository
                        .findTopByUserIdAndEffectiveAtIsNotNullOrderByEffectiveAtDesc(
                                userId
                        );

        Integer minimumCalories =
                appliedReview
                        .map(
                                AiPlanReview
                                        ::getRecommendedMinimumCalories
                        )
                        .orElse(null);

        if (minimumCalories == null) {
            minimumCalories =
                    initialPlan.getMinimumCalories();
        }

        Integer maximumCalories =
                appliedReview
                        .map(
                                AiPlanReview
                                        ::getRecommendedMaximumCalories
                        )
                        .orElse(null);

        if (maximumCalories == null) {
            maximumCalories =
                    initialPlan.getMaximumCalories();
        }

        Integer workoutDaysPerWeek =
                appliedReview
                        .map(
                                AiPlanReview
                                        ::getRecommendedWorkoutDaysPerWeek
                        )
                        .orElse(null);

        if (workoutDaysPerWeek == null) {
            workoutDaysPerWeek =
                    userProfile.getWorkoutDaysPerWeek();
        }

        Integer workoutMinutesPerDay =
                appliedReview
                        .map(
                                AiPlanReview
                                        ::getRecommendedWorkoutMinutesPerDay
                        )
                        .orElse(null);

        if (workoutMinutesPerDay == null) {
            workoutMinutesPerDay =
                    userProfile.getWorkoutMinutesPerDay();
        }

        Instant rangeStart =
                reviewStartDate
                        .atStartOfDay(KOREA_ZONE)
                        .toInstant();

        Instant rangeEnd =
                reviewEndDate
                        .atTime(LocalTime.MAX)
                        .atZone(KOREA_ZONE)
                        .toInstant();

        List<MealRecord> mealRecords =
                mealRecordRepository
                        .findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                                userId,
                                rangeStart,
                                rangeEnd
                        );

        List<ExerciseRecord> exerciseRecords =
                exerciseRecordRepository
                        .findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                                userId,
                                rangeStart,
                                rangeEnd
                        );

        List<WeightRecord> weightRecords =
                weightRecordRepository
                        .findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                                userId,
                                rangeStart,
                                rangeEnd
                        );

        Map<LocalDate, BigDecimal> caloriesByDate =
                collectCaloriesByDate(mealRecords);

        Map<LocalDate, Integer> workoutMinutesByDate =
                collectWorkoutMinutesByDate(
                        exerciseRecords
                );

        Map<LocalDate, BigDecimal> weightByDate =
                collectLatestWeightByDate(
                        weightRecords
                );

        List<PlanReviewAiRequest.DailyRecord>
                dailyRecords =
                createDailyRecords(
                        reviewStartDate,
                        reviewEndDate,
                        caloriesByDate,
                        workoutMinutesByDate,
                        weightByDate
                );

        PlanReviewAiRequest.CurrentPlan currentPlan =
                new PlanReviewAiRequest.CurrentPlan(
                        initialPlan.getGoalDirection(),
                        initialPlan.getStartingWeight(),
                        goal.getTargetWeight(),
                        new PlanReviewAiRequest
                                .DailyCalorieRange(
                                minimumCalories,
                                maximumCalories,
                                initialPlan.isEstimated()
                        ),
                        workoutDaysPerWeek,
                        workoutMinutesPerDay
                );

        return new PlanReviewAiRequest(
                reviewStartDate,
                reviewEndDate,
                currentPlan,
                dailyRecords
        );
    }

    private Map<LocalDate, BigDecimal>
            collectCaloriesByDate(
                    List<MealRecord> mealRecords
            ) {
        Map<LocalDate, BigDecimal> result =
                new HashMap<>();

        for (MealRecord mealRecord : mealRecords) {
            List<MealFood> foods =
                    mealFoodRepository
                            .findAllByMealRecordIdOrderByIdAsc(
                                    mealRecord.getId()
                            );

            int calorieSum = 0;
            boolean hasCalorieValue = false;

            for (MealFood food : foods) {
                if (food.getCalories() != null) {
                    calorieSum += food.getCalories();
                    hasCalorieValue = true;
                }
            }

            if (!hasCalorieValue) {
                continue;
            }

            LocalDate recordDate =
                    toKoreaDate(
                            mealRecord.getRecordedAt()
                    );

            result.merge(
                    recordDate,
                    BigDecimal.valueOf(calorieSum),
                    BigDecimal::add
            );
        }

        return result;
    }

    private Map<LocalDate, Integer>
            collectWorkoutMinutesByDate(
                    List<ExerciseRecord> exerciseRecords
            ) {
        Map<LocalDate, Integer> result =
                new HashMap<>();

        for (
                ExerciseRecord exerciseRecord
                : exerciseRecords
        ) {
            LocalDate recordDate =
                    toKoreaDate(
                            exerciseRecord.getRecordedAt()
                    );

            result.merge(
                    recordDate,
                    exerciseRecord.getDurationMinutes(),
                    Integer::sum
            );
        }

        return result;
    }

    private Map<LocalDate, BigDecimal>
            collectLatestWeightByDate(
                    List<WeightRecord> weightRecords
            ) {
        Map<LocalDate, BigDecimal> result =
                new HashMap<>();

        for (WeightRecord weightRecord : weightRecords) {
            LocalDate recordDate =
                    toKoreaDate(
                            weightRecord.getRecordedAt()
                    );

            result.putIfAbsent(
                    recordDate,
                    weightRecord.getWeight()
            );
        }

        return result;
    }

    private List<PlanReviewAiRequest.DailyRecord>
            createDailyRecords(
                    LocalDate reviewStartDate,
                    LocalDate reviewEndDate,
                    Map<LocalDate, BigDecimal>
                            caloriesByDate,
                    Map<LocalDate, Integer>
                            workoutMinutesByDate,
                    Map<LocalDate, BigDecimal>
                            weightByDate
            ) {
        List<PlanReviewAiRequest.DailyRecord> records =
                new ArrayList<>();

        LocalDate currentDate = reviewStartDate;

        while (!currentDate.isAfter(reviewEndDate)) {
            records.add(
                    new PlanReviewAiRequest.DailyRecord(
                            currentDate,
                            weightByDate.get(currentDate),
                            caloriesByDate.get(currentDate),
                            null,
                            workoutMinutesByDate
                                    .getOrDefault(
                                            currentDate,
                                            0
                                    )
                    )
            );

            currentDate = currentDate.plusDays(1);
        }

        return records;
    }

    private LocalDate toKoreaDate(Instant instant) {
        return instant
                .atZone(KOREA_ZONE)
                .toLocalDate();
    }

    private void validateReviewPeriod(
            LocalDate reviewStartDate,
            LocalDate reviewEndDate
    ) {
        if (
                reviewStartDate == null
                || reviewEndDate == null
        ) {
            throw new IllegalArgumentException(
                    "재분석 시작일과 종료일이 필요합니다."
            );
        }

        if (reviewEndDate.isBefore(reviewStartDate)) {
            throw new IllegalArgumentException(
                    "재분석 종료일은 시작일보다 빠를 수 없습니다."
            );
        }

        long reviewDays =
                ChronoUnit.DAYS.between(
                        reviewStartDate,
                        reviewEndDate
                ) + 1;

        if (reviewDays > MAX_REVIEW_DAYS) {
            throw new IllegalArgumentException(
                    "플랜 재분석 기간은 최대 14일입니다."
            );
        }
    }
}