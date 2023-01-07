package com.onthe7.pupsroad.module.resource.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.onthe7.pupsroad.common.domain.dto.PaginationDto.PaginationRequestDto;

public class ResourceQuery {

    @Getter
    @AllArgsConstructor
    public static class UserDefaultGalleryResourceElementList {
        private final Long userId;
        private final PaginationRequestDto paginationRequest;
    }
}
