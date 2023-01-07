package com.onthe7.pupsroad.module.gallery.infrastructure;

import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryToResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryToResourceRepository extends JpaRepository<GalleryToResourceEntity, Long> {
    List<GalleryToResourceEntity> getGalleryToResourceEntityByGalleryIdAndResourceIdIn(Long userId, List<Long> resourceIds);
    Optional<GalleryToResourceEntity> findByGalleryId(Long galleryId);
}
