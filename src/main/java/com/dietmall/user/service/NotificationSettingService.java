package com.dietmall.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.dto.NotificationSettingResponse;
import com.dietmall.user.dto.NotificationSettingUpdateRequest;
import com.dietmall.user.entity.NotificationSetting;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.NotificationSettingRepository;
import com.dietmall.user.repository.UserRepository;

@Service
public class NotificationSettingService {

    private final NotificationSettingRepository notificationSettingRepository;
    private final UserRepository userRepository;


    public NotificationSettingService(
            NotificationSettingRepository notificationSettingRepository,
            UserRepository userRepository) {

        this.notificationSettingRepository =
                notificationSettingRepository;

        this.userRepository =
                userRepository;
    }


    @Transactional
    public NotificationSettingResponse getSetting(Long userId) {

        NotificationSetting setting =
                getOrCreateSetting(userId);

        return toResponse(setting);
    }


    @Transactional
    public NotificationSettingResponse updateSetting(
            Long userId,
            NotificationSettingUpdateRequest request) {

        NotificationSetting setting =
                getOrCreateSetting(userId);

        setting.update(
                request.isMealReminderEnabled(),
                request.isExerciseReminderEnabled()
        );

        return toResponse(setting);
    }


    private NotificationSetting getOrCreateSetting(
            Long userId) {

        return notificationSettingRepository
                .findByUserId(userId)
                .orElseGet(() -> {

                    User user =
                            userRepository
                                    .findById(userId)
                                    .orElseThrow(() ->
                                            new IllegalArgumentException(
                                                    "사용자를 찾을 수 없습니다."
                                            )
                                    );

                    NotificationSetting newSetting =
                            new NotificationSetting(user);

                    return notificationSettingRepository
                            .save(newSetting);
                });
    }


    private NotificationSettingResponse toResponse(
            NotificationSetting setting) {

        return new NotificationSettingResponse(
                setting.isMealReminderEnabled(),
                setting.isExerciseReminderEnabled()
        );
    }
}