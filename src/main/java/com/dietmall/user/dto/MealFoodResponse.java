package com.dietmall.user.dto;

import com.dietmall.user.entity.CalorieSource;
import com.dietmall.user.entity.FoodAnalysisStatus;
import com.dietmall.user.entity.MealFood;

public class MealFoodResponse {

    private final Long id;
    private final String foodName;
    private final String amountDescription;
    private final Integer calories;
    private final CalorieSource calorieSource;
    private final FoodAnalysisStatus analysisStatus;

    public MealFoodResponse(
            Long id,
            String foodName,
            String amountDescription,
            Integer calories,
            CalorieSource calorieSource,
            FoodAnalysisStatus analysisStatus) {

        this.id = id;
        this.foodName = foodName;
        this.amountDescription = amountDescription;
        this.calories = calories;
        this.calorieSource = calorieSource;
        this.analysisStatus = analysisStatus;
    }

    public static MealFoodResponse from(
            MealFood mealFood) {

        return new MealFoodResponse(
                mealFood.getId(),
                mealFood.getFoodName(),
                mealFood.getAmountDescription(),
                mealFood.getCalories(),
                mealFood.getCalorieSource(),
                mealFood.getAnalysisStatus()
        );
    }

    public Long getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getAmountDescription() {
        return amountDescription;
    }

    public Integer getCalories() {
        return calories;
    }

    public CalorieSource getCalorieSource() {
        return calorieSource;
    }

    public FoodAnalysisStatus getAnalysisStatus() {
        return analysisStatus;
    }
}