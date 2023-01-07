package com.onthe7.pupsroad.module.resource.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onthe7.pupsroad.common.domain.dto.Cursor;
import com.onthe7.pupsroad.module.resource.domain.enums.ResourceType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class ResourceQueryResponse {

    @Getter
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class ResourceElement implements Cursor {
        private final Long resourceId;
        private final ResourceType type;
        private final String url;
        private final String originalFilename;
        private final LocalDateTime createdAt;

        @JsonIgnore
        private final String cursor;

        @QueryProjection
        public ResourceElement(Long resourceId, ResourceType type, String url,
                               String originalFilename, LocalDateTime createdAt, String cursor) {
            this.resourceId = resourceId;
            this.type = type;
            this.url = url;
            this.originalFilename = originalFilename;
            this.createdAt = createdAt;
            this.cursor = cursor;
        }

        @QueryProjection
        public ResourceElement(Long resourceId, ResourceType type, String url, String originalFilename, LocalDateTime createdAt) {
            this.resourceId = resourceId;
            this.type = type;
            this.url = url;
            this.originalFilename = originalFilename;
            this.createdAt = createdAt;
            this.cursor = null;
        }
    }
}
