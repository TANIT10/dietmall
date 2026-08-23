package com.dietmall.user.entity;

import java.math.BigDecimal;

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

    // 성별
    @Enumerated(EnumType.STRING)
    @Column(
            name = "gender",
            length = 10
    )
    private Gender gender;

    // 나이
    @Column(name = "age")
    private Integer age;

    // 키(cm)
    @Column(
            name = "height_cm",
            precision = 5,
            scale = 2
    )
    private BigDecimal heightCm;

    // 운동량
    @Enumerated(EnumType.STRING)
    @Column(
            name = "exercise_level",
            nullable = false,
            length = 20
    )
    private ExerciseLevel exerciseLevel;

    // 일주일 운동 가능 일수
    @Column(name = "workout_days_per_week")
    private Integer workoutDaysPerWeek;

    // 하루 운동 가능 시간(분)
    @Column(name = "workout_minutes_per_day")
    private Integer workoutMinutesPerDay;

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
            Gender gender,
            Integer age,
            BigDecimal heightCm,
            ExerciseLevel exerciseLevel,
            Integer workoutDaysPerWeek,
            Integer workoutMinutesPerDay,
            DietDifficulty dietDifficulty,
            AlcoholFrequency alcoholFrequency,
            MealPreference mealPreference
    ) {
        this.user = user;
        this.gender = gender;
        this.age = age;
        this.heightCm = heightCm;
        this.exerciseLevel = exerciseLevel;
        this.workoutDaysPerWeek = workoutDaysPerWeek;
        this.workoutMinutesPerDay = workoutMinutesPerDay;
        this.dietDifficulty = dietDifficulty;
        this.alcoholFrequency = alcoholFrequency;
        this.mealPreference = mealPreference;
    }

    // 생활 습관 프로필 수정
    public void updateProfile(
            ExerciseLevel exerciseLevel,
            DietDifficulty dietDifficulty,
            AlcoholFrequency alcoholFrequency,
            MealPreference mealPreference
    ) {
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

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public BigDecimal getHeightCm() {
        return heightCm;
    }

    public ExerciseLevel getExerciseLevel() {
        return exerciseLevel;
    }

    public Integer getWorkoutDaysPerWeek() {
        return workoutDaysPerWeek;
    }

    public Integer getWorkoutMinutesPerDay() {
        return workoutMinutesPerDay;
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