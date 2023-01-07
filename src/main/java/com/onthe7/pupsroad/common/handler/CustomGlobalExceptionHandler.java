package com.onthe7.pupsroad.common.handler;

import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.exception.BusinessException;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.common.util.RequestIdGenerator;
import com.onthe7.pupsroad.common.vo.ErrorResponse;
import com.onthe7.pupsroad.common.vo.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    private final RequestIdGenerator requestIdGenerator;

    @ExceptionHandler(value = {BusinessException.class})
    protected Response<?> processError(BusinessException ex, HttpServletResponse servletResponse) {
        String requestId = requestIdGenerator.getRequestId();
        ErrorResponse errorResponse = ErrorResponse.from(ex.getErrorCode());
        servletResponse.setStatus(errorResponse.getStatus());
        return Response.error(requestId, errorResponse);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected Response<?> processError(Exception ex, HttpServletResponse servletResponse) {
        ex.printStackTrace();
        String requestId = requestIdGenerator.getRequestId();
        ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.COMMON_SYSTEM_ERROR);
        servletResponse.setStatus(errorResponse.getStatus());
        return Response.error(requestId, errorResponse);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected Response<?> processError(MethodArgumentNotValidException ex, HttpServletResponse servletResponse) {
        ex.printStackTrace();
        String requestId = requestIdGenerator.getRequestId();
        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.CLIENT_INVALID_PARAM, defaultMessage);
        servletResponse.setStatus(errorResponse.getStatus());
        return Response.error(requestId, errorResponse);
    }

    @ExceptionHandler(value = {UIException.class})
    protected Response<?> processError(UIException ex, HttpServletResponse servletResponse) {
        String requestId = requestIdGenerator.getRequestId();
        ErrorResponse errorResponse = ErrorResponse.customErrorMessage(ex.getErrorCode(), ex.getMessage());
        servletResponse.setStatus(errorResponse.getStatus());
        return Response.error(requestId, errorResponse);
    }
}
