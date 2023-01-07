package com.onthe7.pupsroad.module.gallery.infrastructure;

import com.onthe7.pupsroad.module.gallery.domain.dto.request.GalleryQuery;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryElement;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryResourceElement;

import java.util.List;

public interface GalleryCustomRepository {
    List<GalleryElement> getGalleryElementList(GalleryQuery.GalleryElementList query);

    List<GalleryResourceElement> getGalleryResourceElementList(GalleryQuery.GalleryResourceElementList query);
}
