package com.dietmall.user.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.UserActivity;

public interface UserActivityRepository
        extends JpaRepository<UserActivity, Long> {

    Optional<UserActivity> findByUserId(Long userId);

    List<UserActivity> findAllByLastActiveAtBefore(
            Instant cutoff
    );
}