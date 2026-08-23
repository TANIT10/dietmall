package com.dietmall.user.dto;

import java.math.BigDecimal;

import com.dietmall.user.entity.AlcoholFrequency;
import com.dietmall.user.entity.DietDifficulty;
import com.dietmall.user.entity.ExerciseLevel;
import com.dietmall.user.entity.Gender;
import com.dietmall.user.entity.MealPreference;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class OnboardingRequest {

    // 성별
    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    // 나이
    @NotNull(message = "나이를 입력해주세요.")
    @Min(value = 13, message = "나이는 13세 이상이어야 합니다.")
    @Max(value = 100, message = "나이는 100세 이하여야 합니다.")
    private Integer age;

    // 키(cm)
    @NotNull(message = "키를 입력해주세요.")
    @DecimalMin(
            value = "50.0",
            inclusive = false,
            message = "키는 50cm보다 커야 합니다."
    )
    @DecimalMax(
            value = "250.0",
            message = "키는 250cm 이하여야 합니다."
    )
    @Digits(
            integer = 3,
            fraction = 2,
            message = "키는 소수점 둘째 자리까지 입력해주세요."
    )
    private BigDecimal heightCm;

    // 현재 체중
    @NotNull(message = "현재 체중을 입력해주세요.")
    @DecimalMin(
            value = "20.0",
            inclusive = false,
            message = "현재 체중은 20kg보다 커야 합니다."
    )
    @DecimalMax(
            value = "400.0",
            message = "현재 체중은 400kg 이하여야 합니다."
    )
    @Digits(
            integer = 3,
            fraction = 2,
            message = "현재 체중은 소수점 둘째 자리까지 입력해주세요."
    )
    private BigDecimal currentWeight;

    // 목표 체중
    @NotNull(message = "목표 체중을 입력해주세요.")
    @DecimalMin(
            value = "20.0",
            inclusive = false,
            message = "목표 체중은 20kg보다 커야 합니다."
    )
    @DecimalMax(
            value = "400.0",
            message = "목표 체중은 400kg 이하여야 합니다."
    )
    @Digits(
            integer = 3,
            fraction = 2,
            message = "목표 체중은 소수점 둘째 자리까지 입력해주세요."
    )
    private BigDecimal targetWeight;

    // 목표 기간(주)
    @NotNull(message = "목표 기간을 입력해주세요.")
    @Min(value = 1, message = "목표 기간은 1주 이상이어야 합니다.")
    @Max(value = 104, message = "목표 기간은 104주 이하여야 합니다.")
    private Integer goalDurationWeeks;

    // 운동량
    @NotNull(message = "운동량을 선택해주세요.")
    private ExerciseLevel exerciseLevel;

    // 일주일 운동 가능 일수
    @NotNull(message = "주당 운동 가능 일수를 입력해주세요.")
    @Min(value = 0, message = "운동 가능 일수는 0일 이상이어야 합니다.")
    @Max(value = 7, message = "운동 가능 일수는 7일 이하여야 합니다.")
    private Integer workoutDaysPerWeek;

    // 하루 운동 가능 시간(분)
    @NotNull(message = "하루 운동 가능 시간을 입력해주세요.")
    @Min(value = 0, message = "운동 가능 시간은 0분 이상이어야 합니다.")
    @Max(value = 300, message = "운동 가능 시간은 300분 이하여야 합니다.")
    private Integer workoutMinutesPerDay;

    // 식단 조절 난이도
    @NotNull(message = "식단 조절 난이도를 선택해주세요.")
    private DietDifficulty dietDifficulty;

    // 음주 습관
    @NotNull(message = "음주 습관을 선택해주세요.")
    private AlcoholFrequency alcoholFrequency;

    // 식사 형태 선호
    @NotNull(message = "식사 형태를 선택해주세요.")
    private MealPreference mealPreference;

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(BigDecimal heightCm) {
        this.heightCm = heightCm;
    }

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

    public Integer getGoalDurationWeeks() {
        return goalDurationWeeks;
    }

    public void setGoalDurationWeeks(
            Integer goalDurationWeeks
    ) {
        this.goalDurationWeeks = goalDurationWeeks;
    }

    public ExerciseLevel getExerciseLevel() {
        return exerciseLevel;
    }

    public void setExerciseLevel(
            ExerciseLevel exerciseLevel
    ) {
        this.exerciseLevel = exerciseLevel;
    }

    public Integer getWorkoutDaysPerWeek() {
        return workoutDaysPerWeek;
    }

    public void setWorkoutDaysPerWeek(
            Integer workoutDaysPerWeek
    ) {
        this.workoutDaysPerWeek = workoutDaysPerWeek;
    }

    public Integer getWorkoutMinutesPerDay() {
        return workoutMinutesPerDay;
    }

    public void setWorkoutMinutesPerDay(
            Integer workoutMinutesPerDay
    ) {
        this.workoutMinutesPerDay =
                workoutMinutesPerDay;
    }

    public DietDifficulty getDietDifficulty() {
        return dietDifficulty;
    }

    public void setDietDifficulty(
            DietDifficulty dietDifficulty
    ) {
        this.dietDifficulty = dietDifficulty;
    }

    public AlcoholFrequency getAlcoholFrequency() {
        return alcoholFrequency;
    }

    public void setAlcoholFrequency(
            AlcoholFrequency alcoholFrequency
    ) {
        this.alcoholFrequency = alcoholFrequency;
    }

    public MealPreference getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(
            MealPreference mealPreference
    ) {
        this.mealPreference = mealPreference;
    }
}