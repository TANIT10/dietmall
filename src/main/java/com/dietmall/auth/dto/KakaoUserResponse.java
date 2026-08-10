package com.dietmall.auth.dto;

public class KakaoUserResponse {

    private Long id;

    private Properties properties;


    public Long getId() {
        return id;
    }


    public Properties getProperties() {
        return properties;
    }


    public static class Properties {

        private String nickname;


        public String getNickname() {
            return nickname;
        }
    }
}