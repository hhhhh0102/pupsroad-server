package com.onthe7.pupsroad.module.user.domain.dto;

import com.onthe7.pupsroad.common.enums.AuthProviderType;
import com.onthe7.pupsroad.common.enums.UserRole;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthDto {

    @Getter
    @AllArgsConstructor
    public static class UserSignUpDto {
        private final AuthProviderType provider;
        private final String principalId;
        private final String nickname;
        private final String email;
        private final String phone;
    }

    @Getter
    @AllArgsConstructor
    public static class UserLoginDto {
        private final AuthProviderType provider;
        private final String principalId;
    }

    @Getter
    public static class UserCredentialDto {
        String clientId;
        UserRole role;
        String principalId;

        @QueryProjection
        public UserCredentialDto(String clientId, UserRole role,
                                 String principalId) {
            this.clientId = clientId;
            this.role = role;
            this.principalId = principalId;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ReissueTokenDto {
        private final String accessToken;
        private final String refreshToken;
    }
}
