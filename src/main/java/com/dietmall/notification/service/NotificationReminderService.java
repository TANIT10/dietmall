package com.dietmall.notification.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationReminderService {

    private final NotificationTargetService notificationTargetService;
    private final NotificationSendService notificationSendService;


    public NotificationReminderService(
            NotificationTargetService notificationTargetService,
            NotificationSendService notificationSendService) {

        this.notificationTargetService =
                notificationTargetService;

        this.notificationSendService =
                notificationSendService;
    }


    public void sendMealReminder(Long userId) {

        if (!notificationTargetService
                .shouldSendMealReminder(userId)) {

            return;
        }

        notificationSendService.sendToUser(
                userId,
                "🥗 회원님! 오늘 식사는 잘 챙기셨어요?",
                "굶는 건 다이어트가 아니라 반칙입니다 😎 PT쌤한테 오늘 먹은 것도 알려주세요!"
        );
    }


    public void sendExerciseReminder(Long userId) {

        if (!notificationTargetService
                .shouldSendExerciseReminder(userId)) {

            return;
        }

        notificationSendService.sendToUser(
                userId,
                "🏃 회원님, 슬슬 움직여볼까요?",
                "딱 10분만 해도 좋습니다. 시작하면 PT쌤이 칭찬해드릴게요 😎"
        );
    }


    public void sendThreeDayComeback(Long userId) {

        notificationSendService.sendToUser(
                userId,
                "👀 회원님... 혹시 포기하셨나요?",
                "PT쌤은 아직 회원님을 포기하지 않았습니다. 슬슬 돌아오실 때 됐는데요?"
        );
    }


    public void sendSevenDayComeback(Long userId) {

        notificationSendService.sendToUser(
                userId,
                "🚨 회원님 실종 신고 직전입니다",
                "일주일째 안 보이시네요. 다이어트몰 PT쌤이 애타게 찾고 있어요. 오늘 다시 시작해봅시다 😎"
        );
    }
}