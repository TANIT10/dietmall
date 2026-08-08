package com.dietmall.user.dto;

import java.math.BigDecimal;

import com.dietmall.user.entity.Goal;

public class GoalResponse {

    private final BigDecimal targetWeight;

    public GoalResponse(BigDecimal targetWeight) {
        this.targetWeight = targetWeight;
    }

    public static GoalResponse from(Goal goal) {
        return new GoalResponse(
                goal.getTargetWeight()
        );
    }

    public BigDecimal getTargetWeight() {
        return targetWeight;
    }
}