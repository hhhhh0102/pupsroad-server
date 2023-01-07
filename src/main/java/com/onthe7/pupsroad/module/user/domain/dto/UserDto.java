package com.onthe7.pupsroad.module.user.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserDto {

    @Getter
    @Setter
    @ToString
    public static class UserInfoDto {
        private String nickname;
        private String phone;
        private String email;
        private String password;
        private String resourceId;
    }
}
