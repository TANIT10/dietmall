package com.dietmall.user.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.dto.MealFoodRequest;
import com.dietmall.user.dto.MealRecordRequest;
import com.dietmall.user.dto.MealRecordResponse;
import com.dietmall.user.dto.MealTodaySummaryResponse;
import com.dietmall.user.entity.CalorieSource;
import com.dietmall.user.entity.FoodAnalysisStatus;
import com.dietmall.user.entity.MealFood;
import com.dietmall.user.entity.MealRecord;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.MealFoodRepository;
import com.dietmall.user.repository.MealRecordRepository;
import com.dietmall.user.repository.UserRepository;

@Service
public class MealService {

    private static final ZoneId KOREA_ZONE =
            ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;
    private final MealRecordRepository mealRecordRepository;
    private final MealFoodRepository mealFoodRepository;

    public MealService(
            UserRepository userRepository,
            MealRecordRepository mealRecordRepository,
            MealFoodRepository mealFoodRepository) {

        this.userRepository = userRepository;
        this.mealRecordRepository = mealRecordRepository;
        this.mealFoodRepository = mealFoodRepository;
    }

    // 식단 기록 추가
    @Transactional
    public MealRecordResponse addMeal(
            Long userId,
            MealRecordRequest request) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        MealRecord mealRecord =
                new MealRecord(
                        user,
                        request.getMealType(),
                        null,
                        Instant.now()
                );

        MealRecord savedMealRecord =
                mealRecordRepository.save(
                        mealRecord
                );

        List<MealFood> savedMealFoods =
                new ArrayList<>();

        for (MealFoodRequest foodRequest
                : request.getFoods()) {

            Integer calories =
                    foodRequest.getCalories();

            CalorieSource calorieSource;
            FoodAnalysisStatus analysisStatus;

            // 사용자가 칼로리를 알고 직접 입력한 경우
            if (calories != null) {

                calorieSource =
                        CalorieSource.USER_INPUT;

                analysisStatus =
                        FoodAnalysisStatus.NOT_REQUIRED;

            } else {

                // 칼로리를 모르는 경우
                // 나중에 AI 분석 대상
                calorieSource =
                        CalorieSource.NONE;

                analysisStatus =
                        FoodAnalysisStatus.PENDING;
            }

            MealFood mealFood =
                    new MealFood(
                            savedMealRecord,
                            foodRequest.getFoodName(),
                            foodRequest.getAmountDescription(),
                            calories,
                            calorieSource,
                            analysisStatus
                    );

            MealFood savedMealFood =
                    mealFoodRepository.save(
                            mealFood
                    );

            savedMealFoods.add(
                    savedMealFood
            );
        }

        return MealRecordResponse.from(
                savedMealRecord,
                savedMealFoods
        );
    }

    // 내 식단 전체 조회
    @Transactional(readOnly = true)
    public List<MealRecordResponse> getAllMeals(
            Long userId) {

        List<MealRecord> mealRecords =
                mealRecordRepository
                        .findAllByUserIdOrderByRecordedAtDesc(
                                userId
                        );

        return convertToResponses(
                mealRecords
        );
    }

    // 오늘 식단 조회
    @Transactional(readOnly = true)
    public List<MealRecordResponse> getTodayMeals(
            Long userId) {

        LocalDate today =
                LocalDate.now(KOREA_ZONE);

        List<MealRecord> mealRecords =
                getMealRecordsByDate(
                        userId,
                        today
                );

        return convertToResponses(
                mealRecords
        );
    }

    // 특정 날짜 식단 조회
    @Transactional(readOnly = true)
    public List<MealRecordResponse> getMealsByDate(
            Long userId,
            LocalDate date) {

        List<MealRecord> mealRecords =
                getMealRecordsByDate(
                        userId,
                        date
                );

        return convertToResponses(
                mealRecords
        );
    }

    // 오늘 식단 요약
    @Transactional(readOnly = true)
    public MealTodaySummaryResponse getTodaySummary(
            Long userId) {

        LocalDate today =
                LocalDate.now(KOREA_ZONE);

        List<MealRecord> mealRecords =
                getMealRecordsByDate(
                        userId,
                        today
                );

        int mealCount =
                mealRecords.size();

        int foodCount = 0;
        int knownCalories = 0;
        int unresolvedFoodCount = 0;

        for (MealRecord mealRecord
                : mealRecords) {

            List<MealFood> mealFoods =
                    mealFoodRepository
                            .findAllByMealRecordIdOrderByIdAsc(
                                    mealRecord.getId()
                            );

            foodCount += mealFoods.size();

            for (MealFood mealFood
                    : mealFoods) {

                Integer calories =
                        mealFood.getCalories();

                if (calories != null) {

                    knownCalories += calories;

                } else {

                    unresolvedFoodCount++;
                }
            }
        }

        boolean calorieCalculationComplete =
                unresolvedFoodCount == 0;

        Integer totalCalories;

        if (calorieCalculationComplete) {

            totalCalories =
                    knownCalories;

        } else {

            totalCalories =
                    null;
        }

        return new MealTodaySummaryResponse(
                mealCount,
                foodCount,
                knownCalories,
                unresolvedFoodCount,
                totalCalories,
                calorieCalculationComplete
        );
    }

    // 특정 날짜의 MealRecord 목록 가져오기
    private List<MealRecord> getMealRecordsByDate(
            Long userId,
            LocalDate date) {

        Instant start =
                date.atStartOfDay(KOREA_ZONE)
                        .toInstant();

        Instant end =
                date.plusDays(1)
                        .atStartOfDay(KOREA_ZONE)
                        .toInstant()
                        .minusNanos(1);

        return mealRecordRepository
                .findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                        userId,
                        start,
                        end
                );
    }

    // MealRecord 목록을 앱 응답 형태로 변환
    private List<MealRecordResponse> convertToResponses(
            List<MealRecord> mealRecords) {

        List<MealRecordResponse> responses =
                new ArrayList<>();

        for (MealRecord mealRecord
                : mealRecords) {

            List<MealFood> mealFoods =
                    mealFoodRepository
                            .findAllByMealRecordIdOrderByIdAsc(
                                    mealRecord.getId()
                            );

            MealRecordResponse response =
                    MealRecordResponse.from(
                            mealRecord,
                            mealFoods
                    );

            responses.add(
                    response
            );
        }

        return responses;
    }
}