package com.dietmall.user.dto;

public class NotificationSettingResponse {

    private final boolean mealReminderEnabled;
    private final boolean exerciseReminderEnabled;


    public NotificationSettingResponse(
            boolean mealReminderEnabled,
            boolean exerciseReminderEnabled) {

        this.mealReminderEnabled =
                mealReminderEnabled;

        this.exerciseReminderEnabled =
                exerciseReminderEnabled;
    }


    public boolean isMealReminderEnabled() {
        return mealReminderEnabled;
    }


    public boolean isExerciseReminderEnabled() {
        return exerciseReminderEnabled;
    }
}