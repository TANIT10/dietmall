package com.dietmall.user.dto;

import com.dietmall.user.entity.AlcoholFrequency;
import com.dietmall.user.entity.DietDifficulty;
import com.dietmall.user.entity.ExerciseLevel;
import com.dietmall.user.entity.MealPreference;

import jakarta.validation.constraints.NotNull;

public class UserProfileUpdateRequest {

    @NotNull(message = "운동량을 선택해주세요.")
    private ExerciseLevel exerciseLevel;

    @NotNull(message = "식단 조절 난이도를 선택해주세요.")
    private DietDifficulty dietDifficulty;

    @NotNull(message = "음주 습관을 선택해주세요.")
    private AlcoholFrequency alcoholFrequency;

    @NotNull(message = "식사 형태를 선택해주세요.")
    private MealPreference mealPreference;

    public ExerciseLevel getExerciseLevel() {
        return exerciseLevel;
    }

    public void setExerciseLevel(
            ExerciseLevel exerciseLevel) {
        this.exerciseLevel = exerciseLevel;
    }

    public DietDifficulty getDietDifficulty() {
        return dietDifficulty;
    }

    public void setDietDifficulty(
            DietDifficulty dietDifficulty) {
        this.dietDifficulty = dietDifficulty;
    }

    public AlcoholFrequency getAlcoholFrequency() {
        return alcoholFrequency;
    }

    public void setAlcoholFrequency(
            AlcoholFrequency alcoholFrequency) {
        this.alcoholFrequency = alcoholFrequency;
    }

    public MealPreference getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(
            MealPreference mealPreference) {
        this.mealPreference = mealPreference;
    }
}