package com.dietmall.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.Goal;

public interface GoalRepository
        extends JpaRepository<Goal, Long> {

    boolean existsByUserId(Long userId);

    Optional<Goal> findByUserId(Long userId);
}