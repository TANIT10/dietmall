package com.dietmall.notification.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.entity.UserActivity;
import com.dietmall.user.repository.UserActivityRepository;

@Service
public class NotificationInactivityService {

    private final UserActivityRepository userActivityRepository;
    private final NotificationReminderService notificationReminderService;


    public NotificationInactivityService(
            UserActivityRepository userActivityRepository,
            NotificationReminderService notificationReminderService) {

        this.userActivityRepository =
                userActivityRepository;

        this.notificationReminderService =
                notificationReminderService;
    }


    @Transactional(readOnly = true)
    public void sendThreeDayInactiveReminders() {

        Instant now = Instant.now();

        Instant threeDaysAgo =
                now.minus(3, ChronoUnit.DAYS);

        Instant fourDaysAgo =
                now.minus(4, ChronoUnit.DAYS);


        List<UserActivity> activities =
                userActivityRepository
                        .findAllByLastActiveAtBefore(
                                threeDaysAgo
                        );


        for (UserActivity activity : activities) {

            Instant lastActiveAt =
                    activity.getLastActiveAt();


            if (lastActiveAt.isAfter(fourDaysAgo)) {

                notificationReminderService
                        .sendThreeDayComeback(
                                activity.getUser().getId()
                        );
            }
        }
    }


    @Transactional(readOnly = true)
    public void sendSevenDayInactiveReminders() {

        Instant now = Instant.now();

        Instant sevenDaysAgo =
                now.minus(7, ChronoUnit.DAYS);

        Instant eightDaysAgo =
                now.minus(8, ChronoUnit.DAYS);


        List<UserActivity> activities =
                userActivityRepository
                        .findAllByLastActiveAtBefore(
                                sevenDaysAgo
                        );


        for (UserActivity activity : activities) {

            Instant lastActiveAt =
                    activity.getLastActiveAt();


            if (lastActiveAt.isAfter(eightDaysAgo)) {

                notificationReminderService
                        .sendSevenDayComeback(
                                activity.getUser().getId()
                        );
            }
        }
    }
}