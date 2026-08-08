package com.dietmall.user.dto;

import com.dietmall.user.entity.AlcoholFrequency;
import com.dietmall.user.entity.DietDifficulty;
import com.dietmall.user.entity.ExerciseLevel;
import com.dietmall.user.entity.MealPreference;
import com.dietmall.user.entity.UserProfile;

public class UserProfileResponse {

    private final ExerciseLevel exerciseLevel;
    private final DietDifficulty dietDifficulty;
    private final AlcoholFrequency alcoholFrequency;
    private final MealPreference mealPreference;

    public UserProfileResponse(
            ExerciseLevel exerciseLevel,
            DietDifficulty dietDifficulty,
            AlcoholFrequency alcoholFrequency,
            MealPreference mealPreference) {

        this.exerciseLevel = exerciseLevel;
        this.dietDifficulty = dietDifficulty;
        this.alcoholFrequency = alcoholFrequency;
        this.mealPreference = mealPreference;
    }

    public static UserProfileResponse from(
            UserProfile userProfile) {

        return new UserProfileResponse(
                userProfile.getExerciseLevel(),
                userProfile.getDietDifficulty(),
                userProfile.getAlcoholFrequency(),
                userProfile.getMealPreference()
        );
    }

    public ExerciseLevel getExerciseLevel() {
        return exerciseLevel;
    }

    public DietDifficulty getDietDifficulty() {
        return dietDifficulty;
    }

    public AlcoholFrequency getAlcoholFrequency() {
        return alcoholFrequency;
    }

    public MealPreference getMealPreference() {
        return mealPreference;
    }
}