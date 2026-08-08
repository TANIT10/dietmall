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
@Table(name = "meal_records")
public class MealRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 사용자의 식단 기록인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    // 아침 / 점심 / 저녁 / 간식
    @Enumerated(EnumType.STRING)
    @Column(
            name = "meal_type",
            nullable = false,
            length = 20
    )
    private MealType mealType;

    // 식사 사진 주소
    // 사진은 선택사항이므로 null 가능
    @Column(
            name = "image_url",
            length = 500
    )
    private String imageUrl;

    // 식단 기록 시각
    @Column(
            name = "recorded_at",
            nullable = false
    )
    private Instant recordedAt;

    protected MealRecord() {
    }

    public MealRecord(
            User user,
            MealType mealType,
            String imageUrl,
            Instant recordedAt) {

        this.user = user;
        this.mealType = mealType;
        this.imageUrl = imageUrl;
        this.recordedAt = recordedAt;
    }

    // 28단계에서 사진 업로드가 연결되면 사용
    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public MealType getMealType() {
        return mealType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}