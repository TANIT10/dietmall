package com.dietmall.user.entity;

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
@Table(name = "meal_foods")
public class MealFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 식사 기록에 포함된 음식인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "meal_record_id",
            nullable = false
    )
    private MealRecord mealRecord;

    // 음식 이름
    // 예: 제육볶음, 마라탕, 닭가슴살
    @Column(
            name = "food_name",
            nullable = false,
            length = 100
    )
    private String foodName;

    // 음식 양 또는 설명
    // 예: 1인분, 150g, 반 공기
    // 선택사항
    @Column(
            name = "amount_description",
            length = 100
    )
    private String amountDescription;

    // 칼로리
    // 사용자가 모르면 null 가능
    @Column(
            name = "calories"
    )
    private Integer calories;

    // 칼로리 값이 어디에서 왔는지
    @Enumerated(EnumType.STRING)
    @Column(
            name = "calorie_source",
            nullable = false,
            length = 20
    )
    private CalorieSource calorieSource;

    // AI 칼로리 분석 상태
    @Enumerated(EnumType.STRING)
    @Column(
            name = "analysis_status",
            nullable = false,
            length = 20
    )
    private FoodAnalysisStatus analysisStatus;

    protected MealFood() {
    }

    public MealFood(
            MealRecord mealRecord,
            String foodName,
            String amountDescription,
            Integer calories,
            CalorieSource calorieSource,
            FoodAnalysisStatus analysisStatus) {

        this.mealRecord = mealRecord;
        this.foodName = foodName;
        this.amountDescription = amountDescription;
        this.calories = calories;
        this.calorieSource = calorieSource;
        this.analysisStatus = analysisStatus;
    }

    // 나중에 AI가 예상 칼로리를 계산했을 때 사용
    public void applyAiEstimatedCalories(
            Integer calories) {

        this.calories = calories;
        this.calorieSource =
                CalorieSource.AI_ESTIMATED;
        this.analysisStatus =
                FoodAnalysisStatus.COMPLETED;
    }

    // AI 분석 실패 시 사용
    public void markAnalysisFailed() {
        this.analysisStatus =
                FoodAnalysisStatus.FAILED;
    }

    public Long getId() {
        return id;
    }

    public MealRecord getMealRecord() {
        return mealRecord;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getAmountDescription() {
        return amountDescription;
    }

    public Integer getCalories() {
        return calories;
    }

    public CalorieSource getCalorieSource() {
        return calorieSource;
    }

    public FoodAnalysisStatus getAnalysisStatus() {
        return analysisStatus;
    }
}