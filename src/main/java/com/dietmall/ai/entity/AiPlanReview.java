package com.dietmall.ai.entity;

import java.time.Instant;
import java.time.LocalDate;

import com.dietmall.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ai_plan_reviews")
public class AiPlanReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(
            name = "review_start_date",
            nullable = false
    )
    private LocalDate reviewStartDate;

    @Column(
            name = "review_end_date",
            nullable = false
    )
    private LocalDate reviewEndDate;

    @Column(
            name = "version",
            nullable = false,
            length = 20
    )
    private String version;

    @Column(
            name = "generated_at",
            nullable = false
    )
    private Instant generatedAt;

    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private String status;

    @Column(
            name = "summary",
            nullable = false,
            length = 1000
    )
    private String summary;

    @Column(
            name = "adjustment_needed",
            nullable = false
    )
    private boolean adjustmentNeeded;

    @Column(
            name = "recommended_minimum_calories"
    )
    private Integer recommendedMinimumCalories;

    @Column(
            name = "recommended_maximum_calories"
    )
    private Integer recommendedMaximumCalories;

    @Column(
            name = "recommended_workout_days_per_week"
    )
    private Integer recommendedWorkoutDaysPerWeek;

    @Column(
            name = "recommended_workout_minutes_per_day"
    )
    private Integer recommendedWorkoutMinutesPerDay;

    @Lob
    @Column(
            name = "response_json",
            nullable = false,
            columnDefinition = "LONGTEXT"
    )
    private String responseJson;

    @Column(
            name = "effective_at"
    )
    private Instant effectiveAt;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    protected AiPlanReview() {
    }

    public AiPlanReview(
            User user,
            LocalDate reviewStartDate,
            LocalDate reviewEndDate,
            String version,
            Instant generatedAt,
            String status,
            String summary,
            boolean adjustmentNeeded,
            Integer recommendedMinimumCalories,
            Integer recommendedMaximumCalories,
            Integer recommendedWorkoutDaysPerWeek,
            Integer recommendedWorkoutMinutesPerDay,
            String responseJson
    ) {
        this.user = user;
        this.reviewStartDate =
                reviewStartDate;
        this.reviewEndDate =
                reviewEndDate;
        this.version = version;
        this.generatedAt = generatedAt;
        this.status = status;
        this.summary = summary;
        this.adjustmentNeeded =
                adjustmentNeeded;
        this.recommendedMinimumCalories =
                recommendedMinimumCalories;
        this.recommendedMaximumCalories =
                recommendedMaximumCalories;
        this.recommendedWorkoutDaysPerWeek =
                recommendedWorkoutDaysPerWeek;
        this.recommendedWorkoutMinutesPerDay =
                recommendedWorkoutMinutesPerDay;
        this.responseJson = responseJson;
        this.createdAt = Instant.now();
    }

    public void applyAdjustment(
            Instant effectiveAt
    ) {
        if (!adjustmentNeeded) {
            throw new IllegalStateException(
                    "적용할 플랜 조정 내용이 없습니다."
            );
        }

        if (this.effectiveAt != null) {
            throw new IllegalStateException(
                    "이미 적용된 플랜 조정입니다."
            );
        }

        this.effectiveAt = effectiveAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getReviewStartDate() {
        return reviewStartDate;
    }

    public LocalDate getReviewEndDate() {
        return reviewEndDate;
    }

    public String getVersion() {
        return version;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getSummary() {
        return summary;
    }

    public boolean isAdjustmentNeeded() {
        return adjustmentNeeded;
    }

    public Integer
            getRecommendedMinimumCalories() {
        return recommendedMinimumCalories;
    }

    public Integer
            getRecommendedMaximumCalories() {
        return recommendedMaximumCalories;
    }

    public Integer
            getRecommendedWorkoutDaysPerWeek() {
        return recommendedWorkoutDaysPerWeek;
    }

    public Integer
            getRecommendedWorkoutMinutesPerDay() {
        return recommendedWorkoutMinutesPerDay;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public Instant getEffectiveAt() {
        return effectiveAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}