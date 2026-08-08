package com.dietmall.user.dto;

import java.math.BigDecimal;

import com.dietmall.user.entity.AlcoholFrequency;
import com.dietmall.user.entity.DietDifficulty;
import com.dietmall.user.entity.ExerciseLevel;
import com.dietmall.user.entity.MealPreference;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OnboardingRequest {

    // 현재 체중
    @NotNull(message = "현재 체중을 입력해주세요.")
    @Positive(message = "현재 체중은 0보다 커야 합니다.")
    @Digits(
            integer = 3,
            fraction = 2,
            message = "현재 체중은 소수점 둘째 자리까지 입력해주세요."
    )
    private BigDecimal currentWeight;

    // 목표 체중
    @NotNull(message = "목표 체중을 입력해주세요.")
    @Positive(message = "목표 체중은 0보다 커야 합니다.")
    @Digits(
            integer = 3,
            fraction = 2,
            message = "목표 체중은 소수점 둘째 자리까지 입력해주세요."
    )
    private BigDecimal targetWeight;

    // 운동량
    @NotNull(message = "운동량을 선택해주세요.")
    private ExerciseLevel exerciseLevel;

    // 식단 조절 난이도
    @NotNull(message = "식단 조절 난이도를 선택해주세요.")
    private DietDifficulty dietDifficulty;

    // 음주 습관
    @NotNull(message = "음주 습관을 선택해주세요.")
    private AlcoholFrequency alcoholFrequency;

    // 식사 형태 선호
    @NotNull(message = "식사 형태를 선택해주세요.")
    private MealPreference mealPreference;

    public BigDecimal getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(BigDecimal currentWeight) {
        this.currentWeight = currentWeight;
    }

    public BigDecimal getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(BigDecimal targetWeight) {
        this.targetWeight = targetWeight;
    }

    public ExerciseLevel getExerciseLevel() {
        return exerciseLevel;
    }

    public void setExerciseLevel(ExerciseLevel exerciseLevel) {
        this.exerciseLevel = exerciseLevel;
    }

    public DietDifficulty getDietDifficulty() {
        return dietDifficulty;
    }

    public void setDietDifficulty(DietDifficulty dietDifficulty) {
        this.dietDifficulty = dietDifficulty;
    }

    public AlcoholFrequency getAlcoholFrequency() {
        return alcoholFrequency;
    }

    public void setAlcoholFrequency(AlcoholFrequency alcoholFrequency) {
        this.alcoholFrequency = alcoholFrequency;
    }

    public MealPreference getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(MealPreference mealPreference) {
        this.mealPreference = mealPreference;
    }
}