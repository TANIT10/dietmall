package com.dietmall.ai.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.dietmall.ai.dto.InitialPlanAiRequest;
import com.dietmall.user.entity.AlcoholFrequency;
import com.dietmall.user.entity.DietDifficulty;
import com.dietmall.user.entity.ExerciseLevel;
import com.dietmall.user.entity.Gender;
import com.dietmall.user.entity.Goal;
import com.dietmall.user.entity.MealPreference;
import com.dietmall.user.entity.UserProfile;
import com.dietmall.user.entity.WeightRecord;


class InitialPlanAiRequestMapperTest {

    private final InitialPlanAiRequestMapper mapper =
            new InitialPlanAiRequestMapper();

    @Test
    void convertsUserDataToFastApiRequest() {
        UserProfile userProfile = new UserProfile(
                null,
                Gender.MALE,
                30,
                new BigDecimal("175.00"),
                ExerciseLevel.HIGH,
                4,
                45,
                DietDifficulty.values()[0],
                AlcoholFrequency.values()[0],
                MealPreference.values()[0]
        );

        Goal goal = new Goal(
                null,
                new BigDecimal("75.00"),
                20
        );

        WeightRecord latestWeightRecord =
                new WeightRecord(
                        null,
                        new BigDecimal("85.00"),
                        Instant.parse(
                                "2026-08-24T00:00:00Z"
                        )
                );

        InitialPlanAiRequest request =
                mapper.toInitialPlanRequest(
                        userProfile,
                        goal,
                        latestWeightRecord
                );

        assertEquals("MALE", request.gender());
        assertEquals(30, request.age());
        assertEquals(
                new BigDecimal("175.00"),
                request.heightCm()
        );
        assertEquals(
                new BigDecimal("85.00"),
                request.currentWeightKg()
        );
        assertEquals(
                new BigDecimal("75.00"),
                request.targetWeightKg()
        );
        assertEquals(20, request.goalDurationWeeks());
        assertEquals("ACTIVE", request.activityLevel());
        assertEquals(4, request.workoutDaysPerWeek());
        assertEquals(45, request.workoutMinutesPerDay());
        assertTrue(request.dietaryRestrictions().isEmpty());
        assertTrue(request.healthNotes().isEmpty());
    }
}