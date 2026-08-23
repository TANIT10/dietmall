package com.dietmall.ai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.ai.entity.AiPlanReview;

public interface AiPlanReviewRepository
        extends JpaRepository<AiPlanReview, Long> {

    Optional<AiPlanReview>
            findTopByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<AiPlanReview>
            findByIdAndUserId(Long reviewId, Long userId);

    List<AiPlanReview>
            findAllByUserIdOrderByCreatedAtDesc(Long userId);
}