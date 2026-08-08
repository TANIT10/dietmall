package com.dietmall.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MealFoodRequest {

    @NotBlank(message = "음식 이름을 입력해주세요.")
    @Size(
            max = 100,
            message = "음식 이름은 100자 이하로 입력해주세요."
    )
    private String foodName;

    @Size(
            max = 100,
            message = "음식 양 설명은 100자 이하로 입력해주세요."
    )
    private String amountDescription;

    @Min(
            value = 0,
            message = "칼로리는 0 이상이어야 합니다."
    )
    @Max(
            value = 10000,
            message = "칼로리는 10000 이하로 입력해주세요."
    )
    private Integer calories;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getAmountDescription() {
        return amountDescription;
    }

    public void setAmountDescription(
            String amountDescription) {

        this.amountDescription = amountDescription;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }
}