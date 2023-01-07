package com.onthe7.pupsroad.module.pet.infrastructure;

import com.onthe7.pupsroad.module.pet.domain.dto.response.PetQueryResponse;
import com.onthe7.pupsroad.module.pet.domain.dto.response.PetQueryResponse.PetResourceInfo;
import com.onthe7.pupsroad.module.pet.domain.dto.response.QPetQueryResponse_PetDetail;
import com.onthe7.pupsroad.module.pet.domain.dto.response.QPetQueryResponse_PetResourceInfo;
import com.onthe7.pupsroad.module.pet.domain.entity.QPetEntity;
import com.onthe7.pupsroad.module.pet.domain.entity.QPetResourceEntity;
import com.onthe7.pupsroad.module.user.domain.entity.QUserEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.onthe7.pupsroad.module.pet.domain.dto.response.PetQueryResponse.PetDetail;

@RequiredArgsConstructor
public class PetCustomRepositoryImpl implements PetCustomRepository{

    private final JPAQueryFactory queryFactory;

    private final QPetEntity pet = QPetEntity.petEntity;
    private final QUserEntity user = QUserEntity.userEntity;
    private final QPetResourceEntity petResource = QPetResourceEntity.petResourceEntity;

    @Override
    public List<PetDetail> getUserPetDetailList(Long userId) {
        return queryFactory
                .select(getPetDetailProjection())
                .from(pet)
                .join(pet.user, user)
                .leftJoin(pet.petResource, petResource)
                .where(user.id.eq(userId))
                .fetch();
    }

    @Override
    public Optional<PetDetail> getPetDetail(Long petId) {
        return Optional.ofNullable(
                queryFactory
                        .select(getPetDetailProjection())
                        .from(pet)
                        .leftJoin(pet.petResource, petResource)
                        .where(pet.id.eq(petId))
                        .fetchOne()
        );
    }

    private Expression<PetDetail> getPetDetailProjection() {
        return new QPetQueryResponse_PetDetail(pet.id, pet.name, pet.breed, pet.birthdate,
                pet.weight, pet.gender, pet.neutralized, getPetResourceInfoProjection());
    }

    private Expression<PetResourceInfo> getPetResourceInfoProjection() {
        return new QPetQueryResponse_PetResourceInfo(petResource.id, petResource.type,
                petResource.url, petResource.originalFilename);
    }
}
