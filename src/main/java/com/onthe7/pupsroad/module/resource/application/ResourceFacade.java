package com.onthe7.pupsroad.module.resource.application;

import com.onthe7.pupsroad.common.external.ncp.NaverObjectStorageService;
import com.onthe7.pupsroad.common.security.service.SecurityUserFacade;
import com.onthe7.pupsroad.module.gallery.domain.service.GalleryCommandService;
import com.onthe7.pupsroad.module.gallery.domain.service.GalleryQueryService;
import com.onthe7.pupsroad.module.resource.domain.dto.request.ResourceCommand.CreateResourceCommand;
import com.onthe7.pupsroad.module.resource.domain.entity.ResourceEntity;
import com.onthe7.pupsroad.module.resource.domain.service.ResourceCommandService;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import static com.onthe7.pupsroad.module.gallery.domain.dto.request.GalleryCommand.CreateGalleryToResourceCommand;

@Service
@RequiredArgsConstructor
public class ResourceFacade {

    private final SecurityUserFacade userFacade;
    private final NaverObjectStorageService naverObjectStorageService;
    private final ResourceCommandService resourceCommandService;
    private final GalleryCommandService galleryCommandService;
    private final GalleryQueryService galleryQueryService;

    @Transactional
    public Long createResource(MultipartFile file) {
        UserEntity user = userFacade.getLoginUser();
        String url = naverObjectStorageService.uploadObject(file);

        CreateResourceCommand command = CreateResourceCommand.toCommand(user, file, url);
        ResourceEntity resource = resourceCommandService.createResource(command);

        Long defaultGalleryId = galleryQueryService.getUserDefaultGalleryIdByUserId(user.getId());
        CreateGalleryToResourceCommand createGalleryResourceCommand = CreateGalleryToResourceCommand
                .toCommand(user.getId(), defaultGalleryId, resource.getId());
        galleryCommandService.createGalleryToResource(createGalleryResourceCommand);
        
        return resource.getId();
    }

}
