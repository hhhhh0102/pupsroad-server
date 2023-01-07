package com.onthe7.pupsroad.common.exception;

import com.onthe7.pupsroad.common.enums.ErrorCode;

public class ResourceException {

    public static class ResourceNotFoundException extends BusinessException {

        public ResourceNotFoundException() {
            super(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    public static class UnsupportedResourceTypeException extends BusinessException {
        public UnsupportedResourceTypeException() {
            super(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
        }
    }
}
