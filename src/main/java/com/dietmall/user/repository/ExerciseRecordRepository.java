package com.dietmall.user.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.ExerciseRecord;

public interface ExerciseRecordRepository
        extends JpaRepository<ExerciseRecord, Long> {

    // 사용자의 모든 운동 기록을 최신순으로 조회
    List<ExerciseRecord> findAllByUserIdOrderByRecordedAtDesc(
            Long userId
    );

    // 특정 시간 범위의 운동 기록을 최신순으로 조회
    List<ExerciseRecord>
            findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                    Long userId,
                    Instant start,
                    Instant end
            );
}