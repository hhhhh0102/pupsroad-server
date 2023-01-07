package com.onthe7.pupsroad.module.gallery.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.common.enums.YesNo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static com.onthe7.pupsroad.module.gallery.domain.dto.request.GalleryCommand.CreateGalleryToResourceCommand;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_gallery_to_resource")
@NoArgsConstructor(access = PROTECTED)
public class GalleryToResourceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long galleryId;

    private Long resourceId;

    public static GalleryToResourceEntity from(CreateGalleryToResourceCommand command) {
        return GalleryToResourceEntity.builder()
                .galleryId(command.getGalleryId())
                .resourceId(command.getResourceId())
                .createdBy(command.getGalleryId()).updatedBy(command.getUserId())
                .build();
    }

    public void delete(Long userId) {
        this.deleted = YesNo.Y;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }
}
