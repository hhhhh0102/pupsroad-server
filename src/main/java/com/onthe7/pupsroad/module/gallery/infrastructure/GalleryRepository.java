package com.onthe7.pupsroad.module.gallery.infrastructure;

import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GalleryRepository extends JpaRepository<GalleryEntity, Long>, GalleryCustomRepository {
    Optional<GalleryEntity> getGalleryEntityByUserIdAndIsDefault(Long userId, YesNo isDefault);
    Optional<GalleryEntity> findByUserIdAndDeletedAtIsNull(Long userId);
}
