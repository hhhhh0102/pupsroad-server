package com.onthe7.pupsroad.module.gallery.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.onthe7.pupsroad.common.domain.dto.PaginationDto.PaginationRequestDto;

public class GalleryQuery {

    @Getter
    @AllArgsConstructor
    public static class GalleryElementList {
        private final Long userId;
        private final PaginationRequestDto paginationRequest;
    }

    @Getter
    @AllArgsConstructor
    public static class GalleryResourceElementList {
        private final Long galleryId;
        private final PaginationRequestDto paginationRequest;
    }
}
