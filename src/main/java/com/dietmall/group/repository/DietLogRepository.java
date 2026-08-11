package com.dietmall.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.group.entity.DietLog;

public interface DietLogRepository
        extends JpaRepository<DietLog, Long> {

    List<DietLog> findAllByGroupIdOrderByCreatedAtDesc(
            Long groupId
    );

    Optional<DietLog> findByIdAndGroupId(
            Long id,
            Long groupId
    );
}