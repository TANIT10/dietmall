package com.dietmall.user.dto;

import java.time.Instant;

import com.dietmall.user.entity.ExerciseRecord;
import com.dietmall.user.entity.ExerciseType;

public class ExerciseRecordResponse {

    private final Long id;
    private final ExerciseType exerciseType;
    private final Integer durationMinutes;
    private final Integer caloriesBurned;
    private final String note;
    private final Instant recordedAt;

    public ExerciseRecordResponse(
            Long id,
            ExerciseType exerciseType,
            Integer durationMinutes,
            Integer caloriesBurned,
            String note,
            Instant recordedAt) {

        this.id = id;
        this.exerciseType = exerciseType;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.note = note;
        this.recordedAt = recordedAt;
    }

    public static ExerciseRecordResponse from(
            ExerciseRecord exerciseRecord) {

        return new ExerciseRecordResponse(
                exerciseRecord.getId(),
                exerciseRecord.getExerciseType(),
                exerciseRecord.getDurationMinutes(),
                exerciseRecord.getCaloriesBurned(),
                exerciseRecord.getNote(),
                exerciseRecord.getRecordedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public String getNote() {
        return note;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}