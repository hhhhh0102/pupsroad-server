package com.onthe7.pupsroad.module.walk.domain.service;

import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.exception.ResourceException.ResourceNotFoundException;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.common.exception.WalkException.WalkNotFoundException;
import com.onthe7.pupsroad.module.resource.domain.dto.response.ResourceQueryResponse.ResourceElement;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkQuery;
import com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.PetWalkElement;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkResourceEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkToPetEntity;
import com.onthe7.pupsroad.module.walk.infrastructure.WalkRepository;
import com.onthe7.pupsroad.module.walk.infrastructure.WalkResourceRepository;
import com.onthe7.pupsroad.module.walk.infrastructure.WalkToPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.WalkDetail;
import static com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.WalkLocationInfo;
import static com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus.IN_PROGRESS;

@Service
@RequiredArgsConstructor
public class WalkQueryService {

    private final WalkRepository walkRepository;
    private final WalkToPetRepository walkToPetRepository;
    private final WalkResourceRepository walkResourceRepository;

    public WalkEntity getWalkEntityByIdOrThrow(Long walkId) {
        return walkRepository.findById(walkId)
                .orElseThrow(WalkNotFoundException::new);
    }

    public List<WalkToPetEntity> getWalkToPetEntitiesByWalkId(WalkEntity walk) {
        return walkToPetRepository.findAllByWalk(walk);
    }

    public List<PetWalkElement> getPetWalkElementListByDateInfo(WalkQuery.PetWalkElementList query) {
        return walkRepository.getPetWalkElementList(query);
    }

    public List<PetWalkElement> getPetWalkElementListByWalkId(Long walkId) {
        return walkRepository.getPetWalkElementList(walkId);
    }

    public List<ResourceElement> getWalkResourceElementListByWalkId(Long walkId) {
        return walkRepository.getPetWalkResourceElementList(walkId);
    }

    public List<WalkLocationInfo> getWalkElementListByLocationInfo(WalkQuery.WalkLocationInfoList query) {
        return walkRepository.getAroundPetLocationInfo(query);
    }

    public WalkToPetEntity getWalkToPetEntityByWalkIdAndPetId(WalkQuery.PetToWalkEntity query) {
        return walkRepository.getWalkToPetEntity(query)
                .orElseThrow(() -> new UIException(ErrorCode.COMMON_SYSTEM_ERROR, "산책 정보를 불러올 수 없습니다"));
    }

    public WalkResourceEntity getWalkResourceEntityByIdOrThrow(Long resourceId) {
        return walkResourceRepository.findById(resourceId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public WalkDetail getWalkDetailById(Long walkId) {
        WalkDetail walkDetail = walkRepository
                .getWalkDetailById(walkId).orElseThrow(WalkNotFoundException::new);
        setWalkDetailAggregates(walkDetail);
        return walkDetail;
    }

    private void setWalkDetailAggregates(WalkDetail walkDetail) {
        List<PetWalkElement> petWalkElementList = getPetWalkElementListByWalkId(walkDetail.getWalkId());
        List<ResourceElement> walkResourceElementList = getWalkResourceElementListByWalkId(walkDetail.getWalkId());
        walkDetail.setPetWalkList(petWalkElementList);
        walkDetail.setResourceList(walkResourceElementList.isEmpty() ? null: walkResourceElementList);
    }

    public List<WalkResourceEntity> getWalkResourceListByIds(List<Long> resourceIds) {
        return walkResourceRepository.findByIdIn(resourceIds);
    }

    public WalkEntity getWalkEntityWithResourceAndDetailOrThrow(Long walkId) {
        return walkRepository.findWalkWithWalkToResourceAndDetailById(walkId)
                .orElseThrow(WalkNotFoundException::new);
    }

    public WalkEntity getUserInProgressWalkEntityOrNull(UserEntity user) {
        return walkRepository.findWalkByUserAndStatus(user, IN_PROGRESS)
                .orElse(null);
    }
}