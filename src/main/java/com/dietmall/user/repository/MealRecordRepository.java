package com.dietmall.user.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.MealRecord;

public interface MealRecordRepository
        extends JpaRepository<MealRecord, Long> {

    // 내 식단 기록 전체를 최신순으로 조회
    List<MealRecord> findAllByUserIdOrderByRecordedAtDesc(
            Long userId
    );

    // 특정 날짜 범위의 식단 기록을 최신순으로 조회
    List<MealRecord>
            findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                    Long userId,
                    Instant start,
                    Instant end
            );
}