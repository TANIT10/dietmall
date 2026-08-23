package com.dietmall.ai.entity;

import java.time.Instant;

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
@Table(name = "ai_initial_plans")
public class AiInitialPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 플랜을 생성한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    // AI API 응답 버전
    @Column(
            name = "version",
            nullable = false,
            length = 20
    )
    private String version;

    // AI 서버에서 플랜을 생성한 시각
    @Column(
            name = "generated_at",
            nullable = false
    )
    private Instant generatedAt;

    // 플랜 요약
    @Column(
            name = "summary",
            nullable = false,
            length = 1000
    )
    private String summary;

    // WEIGHT_LOSS, MAINTENANCE, WEIGHT_GAIN
    @Column(
            name = "goal_direction",
            nullable = false,
            length = 30
    )
    private String goalDirection;

    // 하루 권장 최소 칼로리
    @Column(
            name = "minimum_calories",
            nullable = false
    )
    private Integer minimumCalories;

    // 하루 권장 최대 칼로리
    @Column(
            name = "maximum_calories",
            nullable = false
    )
    private Integer maximumCalories;

    // 추정값 여부
    @Column(
            name = "estimated",
            nullable = false
    )
    private boolean estimated;

    // FastAPI가 반환한 전체 응답 JSON
    @Lob
    @Column(
            name = "response_json",
            nullable = false
    )
    private String responseJson;

    // Spring DB에 저장된 시각
    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    protected AiInitialPlan() {
    }

    public AiInitialPlan(
            User user,
            String version,
            Instant generatedAt,
            String summary,
            String goalDirection,
            Integer minimumCalories,
            Integer maximumCalories,
            boolean estimated,
            String responseJson
    ) {
        this.user = user;
        this.version = version;
        this.generatedAt = generatedAt;
        this.summary = summary;
        this.goalDirection = goalDirection;
        this.minimumCalories = minimumCalories;
        this.maximumCalories = maximumCalories;
        this.estimated = estimated;
        this.responseJson = responseJson;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getVersion() {
        return version;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public String getSummary() {
        return summary;
    }

    public String getGoalDirection() {
        return goalDirection;
    }

    public Integer getMinimumCalories() {
        return minimumCalories;
    }

    public Integer getMaximumCalories() {
        return maximumCalories;
    }

    public boolean isEstimated() {
        return estimated;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}