package com.dietmall.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardResponse {

    private final LocalDate date;
    private final WeightSummary weight;
    private final MealSummary meal;
    private final ExerciseSummary exercise;

    public DashboardResponse(
            LocalDate date,
            WeightSummary weight,
            MealSummary meal,
            ExerciseSummary exercise) {

        this.date = date;
        this.weight = weight;
        this.meal = meal;
        this.exercise = exercise;
    }

    public LocalDate getDate() {
        return date;
    }

    public WeightSummary getWeight() {
        return weight;
    }

    public MealSummary getMeal() {
        return meal;
    }

    public ExerciseSummary getExercise() {
        return exercise;
    }

    public static class WeightSummary {

        private final BigDecimal currentWeight;
        private final BigDecimal targetWeight;
        private final BigDecimal remainingWeight;

        public WeightSummary(
                BigDecimal currentWeight,
                BigDecimal targetWeight,
                BigDecimal remainingWeight) {

            this.currentWeight = currentWeight;
            this.targetWeight = targetWeight;
            this.remainingWeight = remainingWeight;
        }

        public BigDecimal getCurrentWeight() {
            return currentWeight;
        }

        public BigDecimal getTargetWeight() {
            return targetWeight;
        }

        public BigDecimal getRemainingWeight() {
            return remainingWeight;
        }
    }

    public static class MealSummary {

        private final int mealCount;
        private final int foodCount;
        private final int knownCalories;
        private final int unresolvedFoodCount;
        private final Integer totalCalories;
        private final boolean calorieCalculationComplete;

        public MealSummary(
                int mealCount,
                int foodCount,
                int knownCalories,
                int unresolvedFoodCount,
                Integer totalCalories,
                boolean calorieCalculationComplete) {

            this.mealCount = mealCount;
            this.foodCount = foodCount;
            this.knownCalories = knownCalories;
            this.unresolvedFoodCount = unresolvedFoodCount;
            this.totalCalories = totalCalories;
            this.calorieCalculationComplete = calorieCalculationComplete;
        }

        public int getMealCount() {
            return mealCount;
        }

        public int getFoodCount() {
            return foodCount;
        }

        public int getKnownCalories() {
            return knownCalories;
        }

        public int getUnresolvedFoodCount() {
            return unresolvedFoodCount;
        }

        public Integer getTotalCalories() {
            return totalCalories;
        }

        public boolean isCalorieCalculationComplete() {
            return calorieCalculationComplete;
        }
    }

    public static class ExerciseSummary {

        private final int exerciseCount;
        private final int totalDurationMinutes;
        private final Integer totalCaloriesBurned;

        public ExerciseSummary(
                int exerciseCount,
                int totalDurationMinutes,
                Integer totalCaloriesBurned) {

            this.exerciseCount = exerciseCount;
            this.totalDurationMinutes = totalDurationMinutes;
            this.totalCaloriesBurned = totalCaloriesBurned;
        }

        public int getExerciseCount() {
            return exerciseCount;
        }

        public int getTotalDurationMinutes() {
            return totalDurationMinutes;
        }

        public Integer getTotalCaloriesBurned() {
            return totalCaloriesBurned;
        }
    }
}