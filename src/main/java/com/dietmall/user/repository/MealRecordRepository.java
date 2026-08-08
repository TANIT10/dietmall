package com.dietmall.user.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.MealRecord;

public interface MealRecordRepository
        extends JpaRepository<MealRecord, Long> {

    // 내 전체 식단 기록
    List<MealRecord> findAllByUserIdOrderByRecordedAtDesc(
            Long userId
    );

    // 특정 날짜의 내 식단 기록
    List<MealRecord> findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
            Long userId,
            Instant start,
            Instant end
    );

    // 특정 식단이 로그인한 사용자의 식단인지 확인
    Optional<MealRecord> findByIdAndUserId(
            Long id,
            Long userId
    );
}