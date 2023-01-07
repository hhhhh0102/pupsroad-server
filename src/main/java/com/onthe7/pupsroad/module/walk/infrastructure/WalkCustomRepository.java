package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkQuery;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkToPetEntity;

import java.util.List;
import java.util.Optional;

import static com.onthe7.pupsroad.module.resource.domain.dto.response.ResourceQueryResponse.ResourceElement;
import static com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.*;

public interface WalkCustomRepository {
    List<PetWalkElement> getPetWalkElementList(WalkQuery.PetWalkElementList query);
    List<PetWalkElement> getPetWalkElementList(Long walkId);
    List<WalkLocationInfo> getAroundPetLocationInfo(WalkQuery.WalkLocationInfoList query);
    Optional<WalkToPetEntity> getWalkToPetEntity(WalkQuery.PetToWalkEntity query);

    Optional<WalkDetail> getWalkDetailById(Long walkId);

    List<ResourceElement> getPetWalkResourceElementList(Long walkId);
}
