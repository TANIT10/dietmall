package com.dietmall.ai.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.dietmall.ai.dto.InitialPlanAiRequest;
import com.dietmall.user.entity.ExerciseLevel;
import com.dietmall.user.entity.Goal;
import com.dietmall.user.entity.UserProfile;
import com.dietmall.user.entity.WeightRecord;


@Component
public class InitialPlanAiRequestMapper {

    public InitialPlanAiRequest toInitialPlanRequest(
            UserProfile userProfile,
            Goal goal,
            WeightRecord latestWeightRecord
    ) {
        Objects.requireNonNull(
                userProfile,
                "사용자 프로필이 필요합니다."
        );
        Objects.requireNonNull(
                goal,
                "사용자 목표가 필요합니다."
        );
        Objects.requireNonNull(
                latestWeightRecord,
                "최근 체중 기록이 필요합니다."
        );

        requireAiProfileFields(userProfile, goal);

        BigDecimal currentWeight =
                Objects.requireNonNull(
                        latestWeightRecord.getWeight(),
                        "현재 체중이 필요합니다."
                );

        return new InitialPlanAiRequest(
                userProfile.getGender().name(),
                userProfile.getAge(),
                userProfile.getHeightCm(),
                currentWeight,
                goal.getTargetWeight(),
                goal.getGoalDurationWeeks(),
                mapActivityLevel(
                        userProfile.getExerciseLevel()
                ),
                userProfile.getWorkoutDaysPerWeek(),
                userProfile.getWorkoutMinutesPerDay(),
                List.of(),
                List.of()
        );
    }

    private void requireAiProfileFields(
            UserProfile userProfile,
            Goal goal
    ) {
        Objects.requireNonNull(
                userProfile.getGender(),
                "AI 플랜 생성을 위한 성별 정보가 없습니다."
        );
        Objects.requireNonNull(
                userProfile.getAge(),
                "AI 플랜 생성을 위한 나이 정보가 없습니다."
        );
        Objects.requireNonNull(
                userProfile.getHeightCm(),
                "AI 플랜 생성을 위한 키 정보가 없습니다."
        );
        Objects.requireNonNull(
                userProfile.getExerciseLevel(),
                "AI 플랜 생성을 위한 운동 수준 정보가 없습니다."
        );
        Objects.requireNonNull(
                userProfile.getWorkoutDaysPerWeek(),
                "AI 플랜 생성을 위한 운동 일수 정보가 없습니다."
        );
        Objects.requireNonNull(
                userProfile.getWorkoutMinutesPerDay(),
                "AI 플랜 생성을 위한 운동 시간 정보가 없습니다."
        );
        Objects.requireNonNull(
                goal.getTargetWeight(),
                "AI 플랜 생성을 위한 목표 체중 정보가 없습니다."
        );
        Objects.requireNonNull(
                goal.getGoalDurationWeeks(),
                "AI 플랜 생성을 위한 목표 기간 정보가 없습니다."
        );
    }

    private String mapActivityLevel(
            ExerciseLevel exerciseLevel
    ) {
        return switch (exerciseLevel) {
            case SEDENTARY -> "SEDENTARY";
            case LIGHT -> "LIGHT";
            case MODERATE -> "MODERATE";
            case HIGH -> "ACTIVE";
        };
    }
}