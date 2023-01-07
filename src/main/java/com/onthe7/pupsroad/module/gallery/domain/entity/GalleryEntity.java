package com.onthe7.pupsroad.module.gallery.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.common.enums.YesNo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_gallery")
@NoArgsConstructor(access = PROTECTED)
public class GalleryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long userId;

    private String name;

    @Enumerated(STRING)
    private YesNo isDefault;

    public static GalleryEntity toEntity(Long userId, String nickname) {
        return GalleryEntity.builder()
                .userId(userId).createdBy(userId).updatedBy(userId)
                .name(nickname + " 의 갤러리")
                .isDefault(YesNo.Y)
                .build();
    }

    public void deleteGallery(Long userId) {
        this.deleted = YesNo.Y;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }
}
