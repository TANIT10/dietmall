package com.dietmall.group.dto;

import jakarta.validation.constraints.Size;

public class DietLogCreateRequest {

    @Size(
            max = 500,
            message = "로그 메모는 500자 이하여야 합니다."
    )
    private String memo;


    public DietLogCreateRequest() {
    }


    public String getMemo() {
        return memo;
    }


    public void setMemo(
            String memo) {

        this.memo = memo;
    }
}