package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.common.config.PreparedStatementProperties;
import com.onthe7.pupsroad.common.util.PaginationUtil;
import com.onthe7.pupsroad.module.pet.domain.entity.QPetEntity;
import com.onthe7.pupsroad.module.resource.domain.dto.response.QResourceQueryResponse_ResourceElement;
import com.onthe7.pupsroad.module.user.domain.entity.QUserEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkQuery;
import com.onthe7.pupsroad.module.walk.domain.dto.response.QWalkQueryResponse_PetWalkElement;
import com.onthe7.pupsroad.module.walk.domain.dto.response.QWalkQueryResponse_WalkDetail;
import com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse;
import com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.PetWalkElement;
import com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.WalkLocationInfo;
import com.onthe7.pupsroad.module.walk.domain.entity.*;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.onthe7.pupsroad.module.resource.domain.dto.response.ResourceQueryResponse.ResourceElement;

@RequiredArgsConstructor
public class WalkCustomRepositoryImpl implements WalkCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final JdbcTemplate jdbcTemplate;
    private final PreparedStatementProperties preparedStatementProperties;

    private final QWalkEntity walk = QWalkEntity.walkEntity;
    private final QWalkRouteEntity route = QWalkRouteEntity.walkRouteEntity;
    private final QWalkDetailEntity walkDetail = QWalkDetailEntity.walkDetailEntity;
    private final QPetEntity pet = QPetEntity.petEntity;
    private final QWalkToPetEntity walkToPet = QWalkToPetEntity.walkToPetEntity;
    private final QWalkToResourceEntity walkToResource = QWalkToResourceEntity.walkToResourceEntity;
    private final QWalkResourceEntity resource = QWalkResourceEntity.walkResourceEntity;
    private final QUserEntity user = QUserEntity.userEntity;

    private final StringPath aliasCursor = Expressions.stringPath("cursor");


    @Override
    public List<PetWalkElement> getPetWalkElementList(WalkQuery.PetWalkElementList query) {
        return queryFactory
                .select(getPetWalkElementListProjectionWithCursor())
                .from(pet)
                .leftJoin(walkToPet).on(walkToPet.pet.eq(pet))
                .leftJoin(walkToPet.walk, walk)
                .leftJoin(walkDetail).on(walkDetail.walk.eq(walk))
                .where(
                        pet.id.eq(query.getPetId()),
                        inYearMonth(query.getYear(), query.getMonth()),
                        getCursor().lt(query.getPaginationRequest().getCursor())
                )
                .orderBy(aliasCursor.desc())
                .fetch();
    }

    @Override
    public List<PetWalkElement> getPetWalkElementList(Long walkId) {
        return queryFactory
                .select(getPetWalkElementListProjection())
                .from(walk)
                .join(walkToPet).on(walkToPet.walk.eq(walk))
                .join(walkToPet.pet, pet)
                .join(walkDetail).on(walkDetail.walk.eq(walk))
                .where(
                        walk.id.eq(walkId)
                )
                .fetch();
    }

    public List<WalkLocationInfo> getAroundPetLocationInfo(WalkQuery.WalkLocationInfoList query) {
        return jdbcTemplate.query(preparedStatementProperties.getGetWalkIdByLocationInfo(),
                (rs, rowNum) -> WalkLocationInfo.builder()
                        .walkId(rs.getLong("id"))
                        .latitude(rs.getDouble("lat"))
                        .longitude(rs.getDouble("lon")).build(),
                query.getLatitude(), query.getLongitude(), query.getLatitude(),
                query.getDistance(), query.getLimit(), query.getOffset());
    }

    @Override
    public Optional<WalkToPetEntity> getWalkToPetEntity(WalkQuery.PetToWalkEntity query) {
        return Optional.ofNullable(queryFactory
                .select(walkToPet)
                .from(walkToPet)
                .where(
                        walkToPet.pet.id.eq(query.getPetId()),
                        walkToPet.walk.id.eq(query.getWalkId())
                )
                .fetchOne());
    }

    @Override
    public Optional<WalkQueryResponse.WalkDetail> getWalkDetailById(Long walkId) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QWalkQueryResponse_WalkDetail(walk.id,
                                route.route, walkDetail.memo, user.id))
                        .from(walk)
                        .leftJoin(route).on(route.walk.eq(walk))
                        .leftJoin(walkDetail).on(walkDetail.walk.eq(walk))
                        .leftJoin(walk.user, user)
                        .where(walk.id.eq(walkId))
                        .fetchOne()
        );
    }

    @Override
    public List<ResourceElement> getPetWalkResourceElementList(Long walkId) {
        return queryFactory
                .select(getWalkResourceElementProjection())
                .from(walk)
                .join(walkToResource).on(walkToResource.walk.eq(walk))
                .join(walkToResource.resource, resource)
                .where(
                        walk.id.eq(walkId)
                )
                .fetch();
    }

    private Expression<PetWalkElement> getPetWalkElementListProjectionWithCursor() {
        return new QWalkQueryResponse_PetWalkElement(pet.name, walk.createdAt, walk.status,
                walkDetail.totalTime, walkDetail.totalDistance, walkToPet.calorie,
                getCursorProjection());
    }

    private Expression<PetWalkElement> getPetWalkElementListProjection() {
        return new QWalkQueryResponse_PetWalkElement(pet.name, walk.createdAt, walk.status,
                walkDetail.totalTime, walkDetail.totalDistance, walkToPet.calorie);
    }

    private Expression<ResourceElement> getWalkResourceElementProjection() {
        return new QResourceQueryResponse_ResourceElement(resource.id,
                resource.type, resource.url, resource.originalFilename, resource.createdAt);
    }

    private Expression<String> getCursorProjection() {
        return ExpressionUtils.as(getCursor(), "cursor");
    }

    private StringExpression getCursor() {
            return PaginationUtil.getOlderDataFirstCursor(walk.createdAt, walk.id);
    }

    private BooleanExpression inYearMonth(Integer year, Integer month) {
        LocalDateTime fromDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime toDate = fromDate.plusMonths(1);
        return walk.createdAt.between(fromDate, toDate);
    }
}
