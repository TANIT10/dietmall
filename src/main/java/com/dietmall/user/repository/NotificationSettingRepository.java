package com.dietmall.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.user.entity.NotificationSetting;

public interface NotificationSettingRepository
        extends JpaRepository<NotificationSetting, Long> {

    Optional<NotificationSetting> findByUserId(Long userId);
}