package com.onthe7.pupsroad.module.otp.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OtpDto {

    /**
     * 인증번호 전송 DTO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class SendOtpDto {
        private String phone;
    }

    /**
     * 인증번호 확인 DTO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class CheckOtpDto {
        private String phone;
        private String otp;
    }

}
