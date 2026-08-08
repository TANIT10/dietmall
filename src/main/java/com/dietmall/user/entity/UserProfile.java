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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 사용자의 프로필인지 연결
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    // 운동량
    @Enumerated(EnumType.STRING)
    @Column(
            name = "exercise_level",
            nullable = false,
            length = 20
    )
    private ExerciseLevel exerciseLevel;

    // 식단 조절 난이도
    @Enumerated(EnumType.STRING)
    @Column(
            name = "diet_difficulty",
            nullable = false,
            length = 20
    )
    private DietDifficulty dietDifficulty;

    // 음주 습관
    @Enumerated(EnumType.STRING)
    @Column(
            name = "alcohol_frequency",
            nullable = false,
            length = 20
    )
    private AlcoholFrequency alcoholFrequency;

    // 편의식 / 일반식 선호
    @Enumerated(EnumType.STRING)
    @Column(
            name = "meal_preference",
            nullable = false,
            length = 20
    )
    private MealPreference mealPreference;

    protected UserProfile() {
    }

    public UserProfile(
            User user,
            ExerciseLevel exerciseLevel,
            DietDifficulty dietDifficulty,
            AlcoholFrequency alcoholFrequency,
            MealPreference mealPreference) {

        this.user = user;
        this.exerciseLevel = exerciseLevel;
        this.dietDifficulty = dietDifficulty;
        this.alcoholFrequency = alcoholFrequency;
        this.mealPreference = mealPreference;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ExerciseLevel getExerciseLevel() {
        return exerciseLevel;
    }

    public DietDifficulty getDietDifficulty() {
        return dietDifficulty;
    }

    public AlcoholFrequency getAlcoholFrequency() {
        return alcoholFrequency;
    }

    public MealPreference getMealPreference() {
        return mealPreference;
    }
}