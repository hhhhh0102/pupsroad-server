package com.onthe7.pupsroad.module.resource.interfaces;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class ResourceDto {
    @Getter
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateResourceResponse {
        private final Long resourceId;

        public static CreateResourceResponse success(Long resourceId) {
            return new CreateResourceResponse(resourceId);
        }
    }
}
