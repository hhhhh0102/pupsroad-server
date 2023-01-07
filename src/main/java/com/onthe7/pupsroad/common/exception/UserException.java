package com.onthe7.pupsroad.common.exception;

import com.onthe7.pupsroad.common.enums.ErrorCode;

public class UserException {

    public static class UserNotFoundException extends BusinessException {
        public UserNotFoundException() {
            super(ErrorCode.USER_NOT_FOUND);
        }
    }

    public static class UserNotVerifiedException extends BusinessException {

        public UserNotVerifiedException() {
            super(ErrorCode.USER_EMAIL_NOT_VERIFIED);
        }
    }

    public static class UserNotLoggedInException extends BusinessException {
        public UserNotLoggedInException() {
            super(ErrorCode.USER_NOT_LOGGED_IN);
        }
    }

    public static class UserAlreadySignedUpException extends BusinessException {
        public UserAlreadySignedUpException() {
            super(ErrorCode.USER_ALREADY_SINGED_UP);
        }
    }

    public static class OtpHasAlReadyRequestedException extends BusinessException {
        public OtpHasAlReadyRequestedException() {
            super(ErrorCode.OTP_HAS_ALREADY_REQUESTED);
        }
    }

    public static class OtpNeverSentException extends BusinessException {
        public OtpNeverSentException() {
            super(ErrorCode.OTP_NEVER_SENT);
        }
    }

    public static class OtpCheckFailedException extends BusinessException {
        public OtpCheckFailedException() {
            super(ErrorCode.OTP_CHECK_FAILED);
        }
    }

    public static class ExpiredOtpException extends BusinessException {
        public ExpiredOtpException() {
            super(ErrorCode.OTP_IS_EXPIRED);
        }
    }

    public static class OtpSendFailedException extends BusinessException {
        public OtpSendFailedException() {
            super(ErrorCode.OTP_SEND_FAILED);
        }
    }

    public static class DuplicateNicknameException extends BusinessException {
        public DuplicateNicknameException() {
            super(ErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
