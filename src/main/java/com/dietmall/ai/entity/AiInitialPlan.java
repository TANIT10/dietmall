package com.dietmall.ai.entity;

import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

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
            name = "summary",
            nullable = false,
            length = 1000
    )
    private String summary;

    @Column(
            name = "goal_direction",
            nullable = false,
            length = 30
    )
    private String goalDirection;

    // 초기 플랜 생성 당시의 체중
    @Column(
            name = "starting_weight",
            nullable = false,
            precision = 5,
            scale = 2
    )
    private BigDecimal startingWeight;

    @Column(
            name = "minimum_calories",
            nullable = false
    )
    private Integer minimumCalories;

    @Column(
            name = "maximum_calories",
            nullable = false
    )
    private Integer maximumCalories;

    @Column(
            name = "estimated",
            nullable = false
    )
    private boolean estimated;

    @Lob
    @Column(
            name = "response_json",
            nullable = false
    )
    private String responseJson;

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
            BigDecimal startingWeight,
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
        this.startingWeight = startingWeight;
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

    public BigDecimal getStartingWeight() {
        return startingWeight;
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