package com.onthe7.pupsroad.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Response<T> {

    private int status;
    private String requestId;
    private Object data;

    /**
     * {
     *     "status": 200
     * }
     */
    public static <T> Response<T> success() {
        return success(null, null);
    }

    /**
     * {
     *     "status": 200,
     *     "requestId": "e5a4e8c5-f115-4a45-a3bb-17d09dbc877f"
     * }
     */
    public static <T> Response<T> success(String requestId) {
        return success(requestId, null);
    }

    /**
     * {
     *     "status": 200,
     *     "requestId": "e5a4e8c5-f115-4a45-a3bb-17d09dbc877f"
     *     "data": {
     *         "result": true
     *     }
     * }
     */
    @SuppressWarnings("unchecked")
    public static <T> Response<T> success(String requestId, T data) {
        return (Response<T>) Response.builder().status(200).requestId(requestId).data(data).build();
    }

    /**
     * {
     *     "status": 404,
     *     "data": {
     *         "status": 404,
     *         "error": "NOT_FOUND",
     *         "code": "USER_NOT_FOUND",
     *         "message": "존재하지 않는 사용자입니다."
     *     }
     * }
     */
    public static Response<?> error(ErrorResponse errorResponse) {
        return error(null, errorResponse);
    }

    /**
     * {
     *     "status": 404,
     *     "requestId": "e5a4e8c5-f115-4a45-a3bb-17d09dbc877f",
     *     "data": {
     *         "status": 404,
     *         "error": "NOT_FOUND",
     *         "code": "USER_NOT_FOUND",
     *         "message": "존재하지 않는 사용자입니다."
     *     }
     * }
     */
    public static Response<?> error(String requestId, ErrorResponse errorResponse) {
        return Response.builder()
                .status(errorResponse.getStatus())
                .requestId(requestId)
                .data(errorResponse)
                .build();
    }

    /**
     * {
     *     "status": 404,
     *     "data": {
     *         ~~~~~~~
     *     }
     * }
     */
    public static <T> Response<T> error(int status, T data) {
        return error(status, null, data);
    }

    /**
     * {
     *     "status": 404,
     *     "requestId": "e5a4e8c5-f115-4a45-a3bb-17d09dbc877f",
     *     "data": {
     *         ~~~~~~~
     *     }
     * }
     */
    @SuppressWarnings("unchecked")
    public static <T> Response<T> error(int status, String requestId, T data) {
        return (Response<T>) Response.builder().status(status).requestId(requestId).data(data).build();
    }

}
