package com.dietmall.notification.entity;

import java.time.LocalDateTime;

import com.dietmall.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "push_devices",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_push_device_token",
                        columnNames = "device_token"
                )
        }
)
public class PushDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    /*
     * DB 컬럼명은 기존 호환을 위해 device_token 그대로 사용한다.
     * 하지만 앞으로 이 값에는 Firebase Installation ID(FID)를 저장한다.
     */
    @Column(
            name = "device_token",
            nullable = false,
            length = 500
    )
    private String fid;


    @Column(
            name = "device_type",
            nullable = false,
            length = 20
    )
    private String deviceType;


    @Column(
            name = "enabled",
            nullable = false
    )
    private boolean enabled = true;


    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;


    protected PushDevice() {
    }


    public PushDevice(
            User user,
            String fid,
            String deviceType) {

        this.user = user;
        this.fid = fid;
        this.deviceType = deviceType;
        this.enabled = true;
        this.updatedAt = LocalDateTime.now();
    }


    public void update(
            User user,
            String fid,
            String deviceType) {

        this.user = user;
        this.fid = fid;
        this.deviceType = deviceType;
        this.enabled = true;
        this.updatedAt = LocalDateTime.now();
    }


    public void disable() {
        this.enabled = false;
        this.updatedAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }


    public String getFid() {
        return fid;
    }


    public String getDeviceType() {
        return deviceType;
    }


    public boolean isEnabled() {
        return enabled;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}