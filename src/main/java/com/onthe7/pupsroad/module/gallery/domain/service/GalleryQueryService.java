package com.onthe7.pupsroad.module.gallery.domain.service;

import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.module.gallery.domain.dto.request.GalleryQuery;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryResourceElement;
import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryEntity;
import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryToResourceEntity;
import com.onthe7.pupsroad.module.gallery.infrastructure.GalleryRepository;
import com.onthe7.pupsroad.module.gallery.infrastructure.GalleryToResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryElement;

@Service
@RequiredArgsConstructor
public class GalleryQueryService {

    private final GalleryRepository galleryRepository;
    private final GalleryToResourceRepository galleryToResourceRepository;

    public List<GalleryElement> getGalleryElementList(GalleryQuery.GalleryElementList query) {
        return galleryRepository.getGalleryElementList(query);
    }

    public Long getUserDefaultGalleryIdByUserId(Long userId) {
        return galleryRepository.getGalleryEntityByUserIdAndIsDefault(userId, YesNo.Y)
                .map(GalleryEntity::getId)
                .orElseThrow(() -> new UIException(ErrorCode.COMMON_SYSTEM_ERROR, "사용자의 갤러리 정보를 찾을 수 없습니다"));
    }

    public GalleryEntity getGalleryEntity(Long galleryId) {
        return galleryRepository.findById(galleryId)
                .orElseThrow(() -> new UIException(ErrorCode.COMMON_SYSTEM_ERROR, "갤러리를 찾을 수 없습니다"));
    }

    public List<GalleryResourceElement> getGalleryResourceElementList(GalleryQuery.GalleryResourceElementList query) {
        return galleryRepository.getGalleryResourceElementList(query);
    }

    public List<GalleryToResourceEntity> getGalleryToResourceEntityList(Long galleryId, List<Long> resourceIds) {
        return galleryToResourceRepository.getGalleryToResourceEntityByGalleryIdAndResourceIdIn(galleryId, resourceIds);
    }
}
