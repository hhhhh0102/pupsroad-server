package com.onthe7.pupsroad.module.gallery.domain.dto.request;

import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryToResourceEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

public class GalleryCommand {
    @Getter
    @Builder(access = PRIVATE)
    public static class CreateGalleryToResourceCommand {
        private final Long userId;
        private final Long galleryId;
        private final Long resourceId;

        public static CreateGalleryToResourceCommand toCommand(Long userId, Long galleryId, Long resourceId) {
            return CreateGalleryToResourceCommand.builder()
                    .userId(userId).galleryId(galleryId).resourceId(resourceId)
                    .build();
        }
    }

    @Getter
    @Builder(access = PRIVATE)
    public static class DeleteGalleryToResourceCommand {
        private final List<GalleryToResourceEntity> galleryToResourceList;
        private final Long userId;

        public static DeleteGalleryToResourceCommand toCommand(Long userId, List<GalleryToResourceEntity> galleryToResourceList) {
            return DeleteGalleryToResourceCommand.builder().
                    userId(userId).galleryToResourceList(galleryToResourceList)
                    .build();
        }
    }
}
