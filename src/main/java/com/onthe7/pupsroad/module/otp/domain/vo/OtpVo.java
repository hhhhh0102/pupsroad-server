package com.onthe7.pupsroad.module.otp.domain.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OtpVo {

    /**
     * 인증번호 정보 DTO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class OtpInfo {
        private String otp;
        private String createdAt;
        private String expiredAt;
    }

}
