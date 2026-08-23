package com.dietmall.ai.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.ai.dto.InitialPlanAiRequest;
import com.dietmall.ai.mapper.InitialPlanAiRequestMapper;
import com.dietmall.user.entity.Goal;
import com.dietmall.user.entity.UserProfile;
import com.dietmall.user.entity.WeightRecord;
import com.dietmall.user.repository.GoalRepository;
import com.dietmall.user.repository.UserProfileRepository;
import com.dietmall.user.repository.WeightRecordRepository;


@Service
public class AiContextService {

    private final UserProfileRepository userProfileRepository;
    private final GoalRepository goalRepository;
    private final WeightRecordRepository weightRecordRepository;
    private final InitialPlanAiRequestMapper initialPlanAiRequestMapper;

    public AiContextService(
            UserProfileRepository userProfileRepository,
            GoalRepository goalRepository,
            WeightRecordRepository weightRecordRepository,
            InitialPlanAiRequestMapper initialPlanAiRequestMapper
    ) {
        this.userProfileRepository = userProfileRepository;
        this.goalRepository = goalRepository;
        this.weightRecordRepository = weightRecordRepository;
        this.initialPlanAiRequestMapper =
                initialPlanAiRequestMapper;
    }

    @Transactional(readOnly = true)
    public InitialPlanAiRequest buildInitialPlanRequest(
            Long userId
    ) {
        UserProfile userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "사용자 프로필을 찾을 수 없습니다."
                        )
                );

        Goal goal = goalRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "사용자 목표를 찾을 수 없습니다."
                        )
                );

        WeightRecord latestWeightRecord =
                weightRecordRepository
                        .findTopByUserIdOrderByRecordedAtDesc(
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "최근 체중 기록을 찾을 수 없습니다."
                                )
                        );

        return initialPlanAiRequestMapper
                .toInitialPlanRequest(
                        userProfile,
                        goal,
                        latestWeightRecord
                );
    }
}