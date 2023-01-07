package com.onthe7.pupsroad.common.exception;

import com.onthe7.pupsroad.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class UIException extends RuntimeException {
    private ErrorCode errorCode;

    public UIException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
