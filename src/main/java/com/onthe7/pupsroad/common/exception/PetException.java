package com.onthe7.pupsroad.common.exception;

import com.onthe7.pupsroad.common.enums.ErrorCode;

public class PetException {

    public static class PetNotFoundException extends BusinessException {

        public PetNotFoundException() {
            super(ErrorCode.PET_NOT_FOUND);
        }
    }
}
