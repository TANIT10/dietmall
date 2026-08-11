package com.dietmall.notification.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.entity.NotificationSetting;
import com.dietmall.user.repository.ExerciseRecordRepository;
import com.dietmall.user.repository.MealRecordRepository;
import com.dietmall.user.repository.NotificationSettingRepository;

@Service
public class NotificationTargetService {

    private static final ZoneId KOREA_ZONE =
            ZoneId.of("Asia/Seoul");


    private final NotificationSettingRepository notificationSettingRepository;
    private final MealRecordRepository mealRecordRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;


    public NotificationTargetService(
            NotificationSettingRepository notificationSettingRepository,
            MealRecordRepository mealRecordRepository,
            ExerciseRecordRepository exerciseRecordRepository) {

        this.notificationSettingRepository =
                notificationSettingRepository;

        this.mealRecordRepository =
                mealRecordRepository;

        this.exerciseRecordRepository =
                exerciseRecordRepository;
    }


    @Transactional(readOnly = true)
    public boolean shouldSendMealReminder(Long userId) {

        NotificationSetting setting =
                getSetting(userId);

        if (!setting.isMealReminderEnabled()) {
            return false;
        }

        Instant start =
                getTodayStart();

        Instant end =
                getTomorrowStart();

        boolean hasMealRecord =
                !mealRecordRepository
                        .findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                                userId,
                                start,
                                end
                        )
                        .isEmpty();

        return !hasMealRecord;
    }


    @Transactional(readOnly = true)
    public boolean shouldSendExerciseReminder(Long userId) {

        NotificationSetting setting =
                getSetting(userId);

        if (!setting.isExerciseReminderEnabled()) {
            return false;
        }

        Instant start =
                getTodayStart();

        Instant end =
                getTomorrowStart();

        boolean hasExerciseRecord =
                !exerciseRecordRepository
                        .findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                                userId,
                                start,
                                end
                        )
                        .isEmpty();

        return !hasExerciseRecord;
    }


    private NotificationSetting getSetting(Long userId) {

        return notificationSettingRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "알림 설정을 찾을 수 없습니다."
                        )
                );
    }


    private Instant getTodayStart() {

        LocalDate today =
                LocalDate.now(KOREA_ZONE);

        return today
                .atStartOfDay(KOREA_ZONE)
                .toInstant();
    }


    private Instant getTomorrowStart() {

        LocalDate tomorrow =
                LocalDate.now(KOREA_ZONE)
                        .plusDays(1);

        return tomorrow
                .atStartOfDay(KOREA_ZONE)
                .toInstant();
    }
}