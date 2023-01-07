package com.onthe7.pupsroad.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400
    CLIENT_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    CLIENT_INVALID_PARAM(HttpStatus.BAD_REQUEST, "%s"),
    OTP_HAS_ALREADY_REQUESTED(HttpStatus.BAD_REQUEST, "인증번호가 이미 요청되었습니다. 1분 후 요청해 주세요."),
    OTP_NEVER_SENT(HttpStatus.BAD_REQUEST, "인증에 실패했습니다, 전화번호를 확인하시고 다시 시도해 주세요."),
    OTP_CHECK_FAILED(HttpStatus.BAD_REQUEST, "인증에 실패했습니다, 인증번호를 확인하시고 다시 시도해 주세요."),
    OTP_IS_EXPIRED(HttpStatus.BAD_REQUEST, "인증번호가 만료되었습니다. 인증번호를 새로 요청하시기 바랍니다."),

    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    USER_EMAIL_NOT_VERIFIED(HttpStatus.NOT_FOUND, "이메일 인증이 필요합니다"),
    PET_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 반려견입니다."),
    WALK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 산책 정보입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 파일입니다."),

    // 401, 403
    AUTH_FAILED(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."),
    USER_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다"),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 409
    USER_ALREADY_SINGED_UP(HttpStatus.CONFLICT, "이미 가입한 사용자입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    WALK_IS_NOT_IN_PROGRESS(HttpStatus.CONFLICT, "현재 진행중인 산책 산책이 아닙니다"),
    WALK_IS_NOT_FINISHED(HttpStatus.CONFLICT, "종료된 산책이 아닙니다. 먼저 산책을 종료 시켜 주세요"),
    USER_ALREADY_WALK_IN_PROGRESS(HttpStatus.CONFLICT, "이미 진행 중인 산책이 있습니다"),

    // 415
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 파일 타입입니다"),

    // 500
    COMMON_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러가 발생했습니다."),
    OTP_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증번호 전송에 실패하였습니다."),
    COMMON_JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    COMMON_JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    COMMON_EXTERNAL_API_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "서버에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Object... arg) {
        return String.format(message, arg);
    }
}
