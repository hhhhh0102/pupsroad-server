package com.onthe7.pupsroad.module.walk.application;

import com.onthe7.pupsroad.common.domain.dto.PaginationDto.PaginationRequestDto;
import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.common.exception.WalkException;
import com.onthe7.pupsroad.common.security.service.SecurityUserFacade;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.pet.domain.service.PetQueryService;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.PetWalkElement;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkResourceEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkToPetEntity;
import com.onthe7.pupsroad.module.walk.domain.service.WalkCommandService;
import com.onthe7.pupsroad.module.walk.domain.service.WalkQueryService;
import com.onthe7.pupsroad.module.walk.domain.service.WalkValidationService;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.CreateWalkRecordRequest;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.CreateWalkRecordRequest.PetWalkRecord;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.CreateWalkResourceRequest;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.CreateWalkRouteRequest;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.StartWalkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.*;
import static com.onthe7.pupsroad.module.walk.domain.dto.request.WalkQuery.*;
import static com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.WalkDetail;
import static com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.WalkLocationInfo;
import static com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus.IN_PROGRESS;

@Service
@RequiredArgsConstructor
public class WalkFacade {

    private final SecurityUserFacade userFacade;
    private final WalkCommandService walkCommandService;
    private final WalkQueryService walkQueryService;
    private final PetQueryService petQueryService;
    private final WalkValidationService walkValidationService;

    @Transactional(rollbackOn = Exception.class)
    public Long startWalk(StartWalkRequest request) {
        UserEntity user = userFacade.getLoginUser();
        WalkEntity inProgressWalk = walkQueryService.getUserInProgressWalkEntityOrNull(user);

        if (Objects.nonNull(inProgressWalk)) {
            throw new WalkException.UserAlreadyWalkInProgressException();
        }

        List<PetEntity> petList = petQueryService.getUserPetEntityListInIds(user, request.getPetIds());
        if (petList.size() != request.getPetIds().size()) {
            throw new UIException(ErrorCode.CLIENT_INVALID_PARAM, "산책 할 수 없는 반려견이 포함되어 있습니다");
        }

        CreateWalkCommand command = CreateWalkCommand.toCommand(user, petList, request);
        return walkCommandService.createWalk(command);
    }

    // 산책을 끝낸다 == 경로를 저장한다?
    @Transactional(rollbackOn = Exception.class)
    public void createRoute(Long walkId, CreateWalkRouteRequest request) {
        UserEntity user = userFacade.getLoginUser();
        WalkEntity walk = walkQueryService.getWalkEntityByIdOrThrow(walkId);
        walkValidationService.userHasWalkCommandPriority(walk, user);
        walkValidationService.eqWalkStatusOrThrow(walk, IN_PROGRESS);
        CreateWalkRouteCommand command = CreateWalkRouteCommand.toCommand(walk, user, request);
        walkCommandService.createWalkRoute(command);
    }


    public void createEvent(Long walkId, WalkDto.CreateWalkEventRequest request) {
        UserEntity user = userFacade.getLoginUser();
        WalkEntity walk = walkQueryService.getWalkEntityByIdOrThrow(walkId);
        walkValidationService.userHasWalkCommandPriority(walk, user);
        walkValidationService.eqWalkStatusOrThrow(walk, IN_PROGRESS);

        PetToWalkEntity query = new PetToWalkEntity(walkId, request.getPetId());
        WalkToPetEntity walkToPet = walkQueryService
                .getWalkToPetEntityByWalkIdAndPetId(query);
        walkCommandService.createEvent(CreateWalkPetEventCommand.toCommand(walkToPet, user.getId(), request));
    }

    // 산책을 끝낸다 == Detail 정보를 입력한다
    @Transactional(rollbackOn = Exception.class)
    public void createWalkDetail(Long walkId, CreateWalkRecordRequest request) {
        UserEntity user = userFacade.getLoginUser();
        WalkEntity walk = walkQueryService.getWalkEntityByIdOrThrow(walkId);

        walkValidationService.userHasWalkCommandPriority(walk, user);
        walkValidationService.eqWalkStatusOrThrow(walk, IN_PROGRESS);

        CreateWalkDetailCommand createWalkDetailCommand = CreateWalkDetailCommand.toCommand(walk, user, request);
        List<Long> petIdList = request.getPetWalkRecords().stream()
                .map(PetWalkRecord::getPetId).collect(Collectors.toList());

        List<PetEntity> pets = petQueryService.getUserPetEntityListInIds(user, petIdList);

        List<WalkToPetEntity> walkToPetList = walkQueryService.getWalkToPetEntitiesByWalkId(walk);
        walkValidationService.validateUpdateWalkToPetCommand(pets, walkToPetList);
        List<UpdateWalkToPetRecordsCommand> updateWalkToPetCommand = UpdateWalkToPetRecordsCommand
                .toCommandList(walkToPetList, request);

        walkCommandService.createWalkDetail(createWalkDetailCommand);
        walkCommandService.updateWalkToPet(updateWalkToPetCommand);
        walk.finishWalk(user.getId());
    }

    @Transactional(rollbackOn = Exception.class)
    public void createWalkResource(Long walkId, CreateWalkResourceRequest request) {
        UserEntity user = userFacade.getLoginUser();
        WalkEntity walk = walkQueryService.getWalkEntityByIdOrThrow(walkId);
        WalkResourceEntity resource = walkQueryService.getWalkResourceEntityByIdOrThrow(request.getResourceId());
        CreateWalkToResourceCommand command = CreateWalkToResourceCommand.toCommand(walk, resource, user);
        walkCommandService.createWalkToResource(command);
    }

    public List<PetWalkElement> getPetWalkHistory(PaginationRequestDto input, Long petId,
                                                  Integer year, Integer month) {
        PetWalkElementList query = new PetWalkElementList(petId, year, month, input);
        return walkQueryService.getPetWalkElementListByDateInfo(query);
    }

    public List<WalkLocationInfo> getWalkElementListByLocationInfo(Long offset, Long limit,
                                                                   Double lat, Double lon, Double distance) {
        WalkLocationInfoList query = new WalkLocationInfoList(lat, lon, distance, offset, limit);
        return walkQueryService.getWalkElementListByLocationInfo(query);
    }

    public WalkDetail getWalkDetailById(Long walkId) {
        return walkQueryService.getWalkDetailById(walkId);
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateWalkDetail(Long walkId, WalkDto.UpdateWalkDetail request) {
        UserEntity user = userFacade.getLoginUser();
        WalkEntity walk = walkQueryService.getWalkEntityWithResourceAndDetailOrThrow(walkId);
        walkValidationService.userHasWalkCommandPriority(walk, user);
        List<WalkResourceEntity> newWalkResourceList = walkQueryService
                .getWalkResourceListByIds(request.getResourceIds());
        UpsertWalkToResourceCommand upsertWalkToResourceCommand = UpsertWalkToResourceCommand
                .toCommand(walk, user, newWalkResourceList);
        UpdateWalkDetailCommand updateWalkDetailCommand = UpdateWalkDetailCommand.toCommand(request, user);
        walkCommandService.upsertWalkToDetail(upsertWalkToResourceCommand);
        walkCommandService.updateWalkDetail(walk.getWalkDetail(), updateWalkDetailCommand);
    }
}
