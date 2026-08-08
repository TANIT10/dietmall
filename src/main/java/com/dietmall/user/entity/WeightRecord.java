package com.dietmall.user.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "weight_records")
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 사용자의 체중 기록인지 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    // 체중
    @Column(
            nullable = false,
            precision = 5,
            scale = 2
    )
    private BigDecimal weight;

    // 기록된 시간
    @Column(
            name = "recorded_at",
            nullable = false
    )
    private Instant recordedAt;

    protected WeightRecord() {
    }

    public WeightRecord(
            User user,
            BigDecimal weight,
            Instant recordedAt) {

        this.user = user;
        this.weight = weight;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}