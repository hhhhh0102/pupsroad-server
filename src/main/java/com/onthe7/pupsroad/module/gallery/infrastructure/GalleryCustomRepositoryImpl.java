package com.onthe7.pupsroad.module.gallery.infrastructure;

import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.common.util.PaginationUtil;
import com.onthe7.pupsroad.module.gallery.domain.dto.request.GalleryQuery;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryElement;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryResourceElement;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.QGalleryQueryResponse_GalleryElement;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.QGalleryQueryResponse_GalleryResourceElement;
import com.onthe7.pupsroad.module.gallery.domain.entity.QGalleryEntity;
import com.onthe7.pupsroad.module.gallery.domain.entity.QGalleryResourceEntity;
import com.onthe7.pupsroad.module.gallery.domain.entity.QGalleryToResourceEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GalleryCustomRepositoryImpl implements GalleryCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QGalleryEntity gallery = QGalleryEntity.galleryEntity;
    private final QGalleryToResourceEntity galleryToResource = QGalleryToResourceEntity.galleryToResourceEntity;
    private final QGalleryResourceEntity resource = QGalleryResourceEntity.galleryResourceEntity;

    private final StringPath aliasCursor = Expressions.stringPath("cursor");


    @Override
    public List<GalleryElement> getGalleryElementList(GalleryQuery.GalleryElementList query) {
        return queryFactory
                .select(getGalleryElementListProjection())
                .from(gallery)
                .leftJoin(galleryToResource).on(galleryToResource.galleryId.eq(gallery.id))
                .where(
                        gallery.userId.eq(query.getUserId()),
                        galleryToResource.deleted.eq(YesNo.N),
                        getGalleryCursor().lt(query.getPaginationRequest().getCursor())
                )
                .orderBy(aliasCursor.desc())
                .fetch();
    }

    @Override
    public List<GalleryResourceElement> getGalleryResourceElementList(GalleryQuery.GalleryResourceElementList query) {
        return queryFactory.select(getGalleryResourceElementListProjection())
                .from(gallery)
                .join(galleryToResource).on(galleryToResource.galleryId.eq(gallery.id))
                .leftJoin(resource).on(galleryToResource.resourceId.eq(resource.id))
                .where(
                        gallery.id.eq(query.getGalleryId()),
                        galleryToResource.deleted.eq(YesNo.N),
                        getGalleryToResourceCursor().lt(query.getPaginationRequest().getCursor())
                )
                .limit(query.getPaginationRequest().getLimit())
                .orderBy(aliasCursor.desc())
                .fetch();
    }

    private Expression<GalleryElement> getGalleryElementListProjection() {
        return new QGalleryQueryResponse_GalleryElement(gallery.id, gallery.name,
                gallery.isDefault, galleryToResource.count(),
                getGalleryCursorProjection());
    }

    private Expression<GalleryResourceElement> getGalleryResourceElementListProjection() {
        return new QGalleryQueryResponse_GalleryResourceElement(resource.id, resource.type,
                resource.url, resource.originalFilename, galleryToResource.createdAt,
                getGalleryToResourceCursorProjection());
    }

    private Expression<String> getGalleryCursorProjection() {
        return ExpressionUtils.as(getGalleryCursor(), "cursor");
    }

    private Expression<String> getGalleryToResourceCursorProjection() {
        return ExpressionUtils.as(getGalleryToResourceCursor(), "cursor");
    }


    private StringExpression getGalleryCursor() {
        return PaginationUtil.getRecentDataFirstCursor(gallery.createdAt, gallery.id);
    }

    private StringExpression getGalleryToResourceCursor() {
        return PaginationUtil.getRecentDataFirstCursor(galleryToResource.createdAt, galleryToResource.id);
    }
}
