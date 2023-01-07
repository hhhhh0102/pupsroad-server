package com.onthe7.pupsroad.module.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthVo {

    @Getter
    @AllArgsConstructor
    public static class UserLoginVo {
        private final String accessToken;
        private final String refreshToken;
        private final String userId;
    }

    @Getter
    @AllArgsConstructor
    public static class ReissueTokenVo {
        String accessToken;
        String refreshToken;
    }
}
