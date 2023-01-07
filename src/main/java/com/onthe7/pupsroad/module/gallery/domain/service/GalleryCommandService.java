package com.onthe7.pupsroad.module.gallery.domain.service;

import com.onthe7.pupsroad.module.gallery.domain.dto.request.GalleryCommand;
import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryEntity;
import com.onthe7.pupsroad.module.gallery.domain.entity.GalleryToResourceEntity;
import com.onthe7.pupsroad.module.gallery.infrastructure.GalleryRepository;
import com.onthe7.pupsroad.module.gallery.infrastructure.GalleryToResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryCommandService {
    
    private final GalleryRepository galleryRepository;
    private final GalleryToResourceRepository galleryToResourceRepository;

    public void createGalleryToResource(GalleryCommand.CreateGalleryToResourceCommand command) {
        GalleryToResourceEntity galleryToResource = GalleryToResourceEntity.from(command);
        galleryToResourceRepository.save(galleryToResource);
    }

    public void deleteGalleryToResourceList(GalleryCommand.DeleteGalleryToResourceCommand command) {
        List<GalleryToResourceEntity> galleryToResourceList = command.getGalleryToResourceList();
        Long userId = command.getUserId();
        galleryToResourceList.forEach(galleryToResource -> galleryToResource.delete(userId));
    }

    public void createUserDefaultGallery(Long userId, String nickname) {
        galleryRepository.save(GalleryEntity.toEntity(userId, nickname));
    }
}
