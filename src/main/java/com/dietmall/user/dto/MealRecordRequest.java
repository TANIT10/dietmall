package com.dietmall.user.dto;

import java.util.List;

import com.dietmall.user.entity.MealType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MealRecordRequest {

    @NotNull(message = "식사 종류를 선택해주세요.")
    private MealType mealType;

    @NotEmpty(message = "음식을 한 개 이상 입력해주세요.")
    @Size(
            max = 20,
            message = "한 번의 식사에는 음식 20개까지 입력할 수 있습니다."
    )
    @Valid
    private List<MealFoodRequest> foods;

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public List<MealFoodRequest> getFoods() {
        return foods;
    }

    public void setFoods(
            List<MealFoodRequest> foods) {

        this.foods = foods;
    }
}