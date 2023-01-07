package com.onthe7.pupsroad.module.gallery.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onthe7.pupsroad.common.domain.dto.Cursor;
import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.module.resource.domain.enums.ResourceType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class GalleryQueryResponse {

    @Getter
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class GalleryElement implements Cursor {

        private final Long galleryId;
        private final String galleryName;
        private final Boolean isDefault;
        private final Long resourceCount;

        @JsonIgnore
        private final String cursor;

        @QueryProjection
        public GalleryElement(Long galleryId, String galleryName,
                              YesNo isDefault, Long resourceCount,
                              String cursor) {
            this.galleryId = galleryId;
            this.galleryName = galleryName;
            this.isDefault = isDefault.equals(YesNo.Y);
            this.resourceCount = resourceCount;
            this.cursor = cursor;
        }
    }

    @Getter
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class GalleryResourceElement implements Cursor {
        private final Long resourceId;
        private final ResourceType type;
        private final String url;
        private final String originalFilename;
        private final LocalDateTime createdAt;

        @JsonIgnore
        private final String cursor;

        @QueryProjection
        public GalleryResourceElement(Long resourceId, ResourceType type, String url,
                               String originalFilename, LocalDateTime createdAt, String cursor) {
            this.resourceId = resourceId;
            this.type = type;
            this.url = url;
            this.originalFilename = originalFilename;
            this.createdAt = createdAt;
            this.cursor = cursor;
        }
    }
}
