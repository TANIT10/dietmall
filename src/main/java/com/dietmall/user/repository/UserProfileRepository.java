package com.dietmall.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.UserProfile;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {

    boolean existsByUserId(Long userId);

    Optional<UserProfile> findByUserId(Long userId);
}