package com.dietmall.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.FoodAnalysisStatus;
import com.dietmall.user.entity.MealFood;

public interface MealFoodRepository
        extends JpaRepository<MealFood, Long> {

    // 특정 식사에 들어 있는 음식 전체 조회
    List<MealFood> findAllByMealRecordIdOrderByIdAsc(
            Long mealRecordId
    );

    // 특정 식사 안에서 특정 AI 분석 상태의 음식 조회
    List<MealFood>
            findAllByMealRecordIdAndAnalysisStatusOrderByIdAsc(
                    Long mealRecordId,
                    FoodAnalysisStatus analysisStatus
            );
}