package com.onthe7.pupsroad.module.gallery.application;

import com.onthe7.pupsroad.common.domain.dto.PaginationDto.PaginationRequestDto;
import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.common.security.service.SecurityUserFacade;
import com.onthe7.pupsroad.module.gallery.domain.dto.request.GalleryCommand;
import com.onthe7.pupsroad.module.gallery.domain.dto.request.GalleryQuery;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryElement;
import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryEntity;
import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryToResourceEntity;
import com.onthe7.pupsroad.module.gallery.domain.service.GalleryCommandService;
import com.onthe7.pupsroad.module.gallery.domain.service.GalleryQueryService;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryResourceElement;

@Service
@RequiredArgsConstructor
public class GalleryFacade {

    private final SecurityUserFacade securityUserFacade;

    private final GalleryCommandService galleryCommandService;
    private final GalleryQueryService galleryQueryService;

    public List<GalleryElement> getUserGalleryList(PaginationRequestDto input) {
        UserEntity user = securityUserFacade.getLoginUser();
        GalleryQuery.GalleryElementList query = new GalleryQuery.GalleryElementList(user.getId(), input);
        return galleryQueryService.getGalleryElementList(query);
    }

    public List<GalleryResourceElement> getUserGalleryResourceElementList(PaginationRequestDto input,
                                                                          Long galleryId) {
        UserEntity user = securityUserFacade.getLoginUser();
        GalleryEntity gallery = galleryQueryService.getGalleryEntity(galleryId);
        if (!gallery.getUserId().equals(user.getId())) {
            throw new UIException(ErrorCode.NO_PERMISSION, "접근 권한이 없습니다");
        }
        GalleryQuery.GalleryResourceElementList query = new GalleryQuery
                .GalleryResourceElementList(gallery.getId(), input);
        return galleryQueryService.getGalleryResourceElementList(query);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteGalleryResource(Long galleryId, List<Long> resourceIds) {
        UserEntity user = securityUserFacade.getLoginUser();
        GalleryEntity gallery = galleryQueryService.getGalleryEntity(galleryId);
        if (!gallery.getUserId().equals(user.getId())) {
            throw new UIException(ErrorCode.NO_PERMISSION, "접근 권한이 없습니다");
        }
        List<GalleryToResourceEntity> galleryToResourceList = galleryQueryService
                .getGalleryToResourceEntityList(gallery.getId(), resourceIds);

        GalleryCommand.DeleteGalleryToResourceCommand command = GalleryCommand.DeleteGalleryToResourceCommand
                .toCommand(user.getId(), galleryToResourceList);

        galleryCommandService.deleteGalleryToResourceList(command);
    }
}
