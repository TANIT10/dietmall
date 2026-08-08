package com.dietmall.user.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.dietmall.user.entity.WeightRecord;

public class WeightRecordResponse {

    private final Long id;
    private final BigDecimal weight;
    private final Instant recordedAt;

    public WeightRecordResponse(
            Long id,
            BigDecimal weight,
            Instant recordedAt) {

        this.id = id;
        this.weight = weight;
        this.recordedAt = recordedAt;
    }

    public static WeightRecordResponse from(
            WeightRecord weightRecord) {

        return new WeightRecordResponse(
                weightRecord.getId(),
                weightRecord.getWeight(),
                weightRecord.getRecordedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}