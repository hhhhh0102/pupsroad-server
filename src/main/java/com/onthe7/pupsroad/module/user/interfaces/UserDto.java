package com.onthe7.pupsroad.module.user.interfaces;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserDto {

    @Getter
    @Setter
    public static class DeleteUserGalleryResourceRequest {
        private List<Long> resourceIds;
    }
}
