package com.dietmall.user.dto;

public class MealTodaySummaryResponse {

    private final int mealCount;
    private final int foodCount;
    private final int knownCalories;
    private final int unresolvedFoodCount;
    private final Integer totalCalories;
    private final boolean calorieCalculationComplete;

    public MealTodaySummaryResponse(
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
        this.calorieCalculationComplete =
                calorieCalculationComplete;
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