package com.dietmall.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.dietmall.dashboard.dto.DashboardResponse;
import com.dietmall.user.dto.ExerciseTodaySummaryResponse;
import com.dietmall.user.dto.GoalResponse;
import com.dietmall.user.dto.MealTodaySummaryResponse;
import com.dietmall.user.dto.WeightRecordResponse;
import com.dietmall.user.service.ExerciseService;
import com.dietmall.user.service.GoalService;
import com.dietmall.user.service.MealService;
import com.dietmall.user.service.WeightService;

@Service
public class DashboardService {

    private static final ZoneId KOREA_ZONE =
            ZoneId.of("Asia/Seoul");

    private final WeightService weightService;
    private final GoalService goalService;
    private final MealService mealService;
    private final ExerciseService exerciseService;

    public DashboardService(
            WeightService weightService,
            GoalService goalService,
            MealService mealService,
            ExerciseService exerciseService) {

        this.weightService = weightService;
        this.goalService = goalService;
        this.mealService = mealService;
        this.exerciseService = exerciseService;
    }

    public DashboardResponse getTodayDashboard(Long userId) {

        LocalDate today = LocalDate.now(KOREA_ZONE);

        WeightRecordResponse currentWeightResponse =
                weightService.getCurrentWeight(userId);

        GoalResponse goalResponse =
                goalService.getGoal(userId);

        MealTodaySummaryResponse mealSummaryResponse =
                mealService.getTodaySummary(userId);

        ExerciseTodaySummaryResponse exerciseSummaryResponse =
                exerciseService.getTodaySummary(userId);

        BigDecimal currentWeight =
                currentWeightResponse.getWeight();

        BigDecimal targetWeight =
                goalResponse.getTargetWeight();
        

        BigDecimal remainingWeight =
                currentWeight
                        .subtract(targetWeight)
                        .max(BigDecimal.ZERO);

        DashboardResponse.WeightSummary weightSummary =
                new DashboardResponse.WeightSummary(
                        currentWeight,
                        targetWeight,
                        remainingWeight
                );

        DashboardResponse.MealSummary mealSummary =
                new DashboardResponse.MealSummary(
                        mealSummaryResponse.getMealCount(),
                        mealSummaryResponse.getFoodCount(),
                        mealSummaryResponse.getKnownCalories(),
                        mealSummaryResponse.getUnresolvedFoodCount(),
                        mealSummaryResponse.getTotalCalories(),
                        mealSummaryResponse.isCalorieCalculationComplete()
                );

        DashboardResponse.ExerciseSummary exerciseSummary =
                new DashboardResponse.ExerciseSummary(
                        exerciseSummaryResponse.getExerciseCount(),
                        exerciseSummaryResponse.getTotalDurationMinutes(),
                        exerciseSummaryResponse.getTotalCaloriesBurned()
                );

        return new DashboardResponse(
                today,
                weightSummary,
                mealSummary,
                exerciseSummary
        );
    }
}