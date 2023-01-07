package com.onthe7.pupsroad.module.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

public class UserVo {

    @Getter
    @Setter
    @ToString
    @Builder
    public static class UserInfoVo {
        private Long id;
        private String nickname;
        private String phone;
        private String email;
        private String provider;
        private Resource resource;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss+09:00", timezone = "Asia/Seoul")
        private LocalDateTime datetime;

        @Getter
        @Setter
        @ToString
        public static class Resource {
            private Long id;
            private String url;
        }
    }
}
