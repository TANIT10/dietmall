package com.dietmall.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.dto.GoalResponse;
import com.dietmall.user.dto.GoalUpdateRequest;
import com.dietmall.user.entity.Goal;
import com.dietmall.user.repository.GoalRepository;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(
            GoalRepository goalRepository) {

        this.goalRepository = goalRepository;
    }

    // 현재 목표 체중 조회
    @Transactional(readOnly = true)
    public GoalResponse getGoal(Long userId) {

        Goal goal = goalRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "목표 정보를 찾을 수 없습니다."
                        )
                );

        return GoalResponse.from(goal);
    }

    // 목표 체중 수정
    @Transactional
    public GoalResponse updateGoal(
            Long userId,
            GoalUpdateRequest request) {

        Goal goal = goalRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "목표 정보를 찾을 수 없습니다."
                        )
                );

        goal.updateTargetWeight(
                request.getTargetWeight()
        );

        return GoalResponse.from(goal);
    }
}