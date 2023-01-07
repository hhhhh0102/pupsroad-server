package com.onthe7.pupsroad.common.exception;

import com.onthe7.pupsroad.common.enums.ErrorCode;

public class WalkException {

    public static class WalkNotFoundException extends BusinessException {
        public WalkNotFoundException() {
            super(ErrorCode.WALK_NOT_FOUND);
        }
    }

    public static class WalkIsNotInProgressException extends BusinessException {
        public WalkIsNotInProgressException() {
            super(ErrorCode.WALK_IS_NOT_IN_PROGRESS);
        }
    }

    public static class WalkIsNotFinishedException extends BusinessException {
        public WalkIsNotFinishedException() {
            super(ErrorCode.WALK_IS_NOT_FINISHED);
        }
    }

    public static class UserAlreadyWalkInProgressException extends BusinessException {
        public UserAlreadyWalkInProgressException() {
            super(ErrorCode.USER_ALREADY_WALK_IN_PROGRESS);
        }
    }
}
