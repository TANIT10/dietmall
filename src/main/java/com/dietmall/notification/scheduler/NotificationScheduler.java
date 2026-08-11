package com.dietmall.notification.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dietmall.notification.service.NotificationInactivityService;
import com.dietmall.notification.service.NotificationReminderService;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

@Component
public class NotificationScheduler {

    private final UserRepository userRepository;
    private final NotificationReminderService notificationReminderService;
    private final NotificationInactivityService notificationInactivityService;


    public NotificationScheduler(
            UserRepository userRepository,
            NotificationReminderService notificationReminderService,
            NotificationInactivityService notificationInactivityService) {

        this.userRepository = userRepository;
        this.notificationReminderService = notificationReminderService;
        this.notificationInactivityService = notificationInactivityService;
    }


    @Scheduled(
            cron = "0 0 22 * * *",
            zone = "Asia/Seoul"
    )
    public void sendDailyRecordReminders() {

        List<User> users =
                userRepository.findAll();


        for (User user : users) {

            Long userId =
                    user.getId();


            notificationReminderService
                    .sendMealReminder(userId);


            notificationReminderService
                    .sendExerciseReminder(userId);
        }
    }


    @Scheduled(
            cron = "0 0 12 * * *",
            zone = "Asia/Seoul"
    )
    public void sendInactiveUserReminders() {

        notificationInactivityService
                .sendThreeDayInactiveReminders();


        notificationInactivityService
                .sendSevenDayInactiveReminders();
    }
}