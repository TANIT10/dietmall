package com.dietmall.user.dto;

public class ExerciseTodaySummaryResponse {

    private final int exerciseCount;
    private final int totalDurationMinutes;
    private final Integer totalCaloriesBurned;

    public ExerciseTodaySummaryResponse(
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