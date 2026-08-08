package com.dietmall.user.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WeightRecordRequest {

    @NotNull(message = "체중을 입력해주세요.")
    @Positive(message = "체중은 0보다 커야 합니다.")
    @Digits(
            integer = 3,
            fraction = 2,
            message = "체중은 소수점 둘째 자리까지 입력해주세요."
    )
    private BigDecimal weight;

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}