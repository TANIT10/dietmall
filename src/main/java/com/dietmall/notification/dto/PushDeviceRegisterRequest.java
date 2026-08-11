package com.dietmall.notification.dto;

import jakarta.validation.constraints.NotBlank;

public class PushDeviceRegisterRequest {

    @NotBlank(message = "Firebase Installation ID는 필수입니다.")
    private String fid;

    @NotBlank(message = "기기 종류는 필수입니다.")
    private String deviceType;


    public PushDeviceRegisterRequest() {
    }


    public String getFid() {
        return fid;
    }


    public void setFid(String fid) {
        this.fid = fid;
    }


    public String getDeviceType() {
        return deviceType;
    }


    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}