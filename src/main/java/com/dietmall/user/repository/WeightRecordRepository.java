package com.dietmall.user.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.WeightRecord;

public interface WeightRecordRepository
        extends JpaRepository<WeightRecord, Long> {

    Optional<WeightRecord>
            findTopByUserIdOrderByRecordedAtDesc(
                    Long userId
            );

    List<WeightRecord>
            findAllByUserIdOrderByRecordedAtDesc(
                    Long userId
            );

    List<WeightRecord>
            findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                    Long userId,
                    Instant start,
                    Instant end
            );
}