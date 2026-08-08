package com.dietmall.user.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercise_records")
public class ExerciseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 사용자의 운동 기록인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    // 운동 종류
    @Enumerated(EnumType.STRING)
    @Column(
            name = "exercise_type",
            nullable = false,
            length = 30
    )
    private ExerciseType exerciseType;

    // 운동 시간(분)
    @Column(
            name = "duration_minutes",
            nullable = false
    )
    private Integer durationMinutes;

    // 예상 소모 칼로리
    // 현재는 없어도 운동 기록 가능
    // 추후 AI / 계산 로직 / HealthKit 등으로 채울 수 있음
    @Column(
            name = "calories_burned"
    )
    private Integer caloriesBurned;

    // 세부 운동이나 사용자 메모
    @Column(
            name = "note",
            length = 200
    )
    private String note;

    // 운동 기록 시각
    @Column(
            name = "recorded_at",
            nullable = false
    )
    private Instant recordedAt;

    protected ExerciseRecord() {
    }

    public ExerciseRecord(
            User user,
            ExerciseType exerciseType,
            Integer durationMinutes,
            String note,
            Instant recordedAt) {

        this.user = user;
        this.exerciseType = exerciseType;
        this.durationMinutes = durationMinutes;
        this.note = note;
        this.recordedAt = recordedAt;
    }

    // 나중에 AI 또는 계산 결과가 나오면 호출
    public void updateCaloriesBurned(
            Integer caloriesBurned) {

        this.caloriesBurned = caloriesBurned;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
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