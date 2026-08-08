package com.dietmall.user.dto;

import java.time.Instant;
import java.util.List;

import com.dietmall.user.entity.MealFood;
import com.dietmall.user.entity.MealRecord;
import com.dietmall.user.entity.MealType;

public class MealRecordResponse {

    private final Long id;
    private final MealType mealType;
    private final String imageUrl;
    private final Instant recordedAt;
    private final List<MealFoodResponse> foods;

    public MealRecordResponse(
            Long id,
            MealType mealType,
            String imageUrl,
            Instant recordedAt,
            List<MealFoodResponse> foods) {

        this.id = id;
        this.mealType = mealType;
        this.imageUrl = imageUrl;
        this.recordedAt = recordedAt;
        this.foods = foods;
    }

    public static MealRecordResponse from(
            MealRecord mealRecord,
            List<MealFood> mealFoods) {

        List<MealFoodResponse> foodResponses =
                mealFoods.stream()
                        .map(MealFoodResponse::from)
                        .toList();

        return new MealRecordResponse(
                mealRecord.getId(),
                mealRecord.getMealType(),
                mealRecord.getImageUrl(),
                mealRecord.getRecordedAt(),
                foodResponses
        );
    }

    public Long getId() {
        return id;
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

    public List<MealFoodResponse> getFoods() {
        return foods;
    }
}