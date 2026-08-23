package com.dietmall.ai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.ai.entity.AiInitialPlan;

public interface AiInitialPlanRepository
        extends JpaRepository<AiInitialPlan, Long> {

    Optional<AiInitialPlan>
            findTopByUserIdOrderByCreatedAtDesc(Long userId);

    List<AiInitialPlan>
            findAllByUserIdOrderByCreatedAtDesc(Long userId);
}