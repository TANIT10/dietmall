package com.dietmall.user.dto;

import com.dietmall.user.entity.ExerciseType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExerciseRecordRequest {

    @NotNull(message = "운동 종류를 선택해주세요.")
    private ExerciseType exerciseType;

    @NotNull(message = "운동 시간을 입력해주세요.")
    @Min(
            value = 1,
            message = "운동 시간은 1분 이상이어야 합니다."
    )
    @Max(
            value = 1440,
            message = "운동 시간은 1440분 이하로 입력해주세요."
    )
    private Integer durationMinutes;

    @Size(
            max = 200,
            message = "메모는 200자 이하로 입력해주세요."
    )
    private String note;

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(
            ExerciseType exerciseType) {

        this.exerciseType = exerciseType;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(
            Integer durationMinutes) {

        this.durationMinutes = durationMinutes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}