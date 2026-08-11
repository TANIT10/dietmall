package com.dietmall.user.dto;

public class NotificationSettingUpdateRequest {

    private boolean mealReminderEnabled;
    private boolean exerciseReminderEnabled;


    public NotificationSettingUpdateRequest() {
    }


    public boolean isMealReminderEnabled() {
        return mealReminderEnabled;
    }


    public void setMealReminderEnabled(
            boolean mealReminderEnabled) {

        this.mealReminderEnabled =
                mealReminderEnabled;
    }


    public boolean isExerciseReminderEnabled() {
        return exerciseReminderEnabled;
    }


    public void setExerciseReminderEnabled(
            boolean exerciseReminderEnabled) {

        this.exerciseReminderEnabled =
                exerciseReminderEnabled;
    }
}