package com.dietmall.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_settings")
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;


    @Column(nullable = false)
    private boolean mealReminderEnabled = true;


    @Column(nullable = false)
    private boolean exerciseReminderEnabled = true;


    protected NotificationSetting() {
    }


    public NotificationSetting(User user) {

        this.user = user;

        this.mealReminderEnabled = true;
        this.exerciseReminderEnabled = true;
    }


    public void update(
            boolean mealReminderEnabled,
            boolean exerciseReminderEnabled) {

        this.mealReminderEnabled =
                mealReminderEnabled;

        this.exerciseReminderEnabled =
                exerciseReminderEnabled;
    }


    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }


    public boolean isMealReminderEnabled() {
        return mealReminderEnabled;
    }


    public boolean isExerciseReminderEnabled() {
        return exerciseReminderEnabled;
    }
}