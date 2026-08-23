package com.dietmall.user.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.dto.OnboardingRequest;
import com.dietmall.user.entity.Goal;
import com.dietmall.user.entity.User;
import com.dietmall.user.entity.UserProfile;
import com.dietmall.user.entity.WeightRecord;
import com.dietmall.user.exception.OnboardingAlreadyCompletedException;
import com.dietmall.user.repository.GoalRepository;
import com.dietmall.user.repository.UserProfileRepository;
import com.dietmall.user.repository.UserRepository;
import com.dietmall.user.repository.WeightRecordRepository;


@Service
public class OnboardingService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final GoalRepository goalRepository;
    private final WeightRecordRepository weightRecordRepository;

    public OnboardingService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            GoalRepository goalRepository,
            WeightRecordRepository weightRecordRepository
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.goalRepository = goalRepository;
        this.weightRecordRepository = weightRecordRepository;
    }

    @Transactional
    public void completeOnboarding(
            Long userId,
            OnboardingRequest request
    ) {
        // 1. 로그인한 사용자 찾기
        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        // 2. 이미 온보딩을 완료했으면 막기
        if (user.isOnboardingCompleted()) {
            throw new OnboardingAlreadyCompletedException();
        }

        // 3. AI 맞춤 계산에 필요한 신체·생활습관 프로필 저장
        UserProfile userProfile = new UserProfile(
                user,
                request.getGender(),
                request.getAge(),
                request.getHeightCm(),
                request.getExerciseLevel(),
                request.getWorkoutDaysPerWeek(),
                request.getWorkoutMinutesPerDay(),
                request.getDietDifficulty(),
                request.getAlcoholFrequency(),
                request.getMealPreference()
        );

        userProfileRepository.save(userProfile);

        // 4. 목표 체중과 목표 기간 저장
        Goal goal = new Goal(
                user,
                request.getTargetWeight(),
                request.getGoalDurationWeeks()
        );

        goalRepository.save(goal);

        // 5. 현재 체중을 첫 번째 체중 기록으로 저장
        WeightRecord weightRecord = new WeightRecord(
                user,
                request.getCurrentWeight(),
                Instant.now()
        );

        weightRecordRepository.save(weightRecord);

        // 6. 사용자 온보딩 완료 처리
        user.completeOnboarding();
    }
}