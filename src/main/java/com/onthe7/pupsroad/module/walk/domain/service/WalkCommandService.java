package com.onthe7.pupsroad.module.walk.domain.service;

import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.*;
import com.onthe7.pupsroad.module.walk.domain.entity.*;
import com.onthe7.pupsroad.module.walk.infrastructure.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalkCommandService {

    private final WalkRepository walkRepository;
    private final WalkToPetRepository walkToPetRepository;
    private final WalkPetEventRepository walkPetEventRepository;
    private final WalkRouteRepository walkRouteRepository;
    private final WalkDetailRepository walkDetailRepository;
    private final WalkToResourceRepository walkToResourceRepository;

    public Long createWalk(CreateWalkCommand command) {
        WalkEntity walk = walkRepository.save(WalkEntity.from(command));
        List<WalkToPetEntity> walkToPet = WalkToPetEntity.from(CreateWalkToPetCommand.toCommand(command, walk));
        walkToPetRepository.saveAll(walkToPet);
        return walk.getId();
    }

    public void createWalkRoute(CreateWalkRouteCommand command) {
        WalkRouteEntity walkRoute = WalkRouteEntity.from(command);
        walkRouteRepository.save(walkRoute);
    }

    public void createWalkDetail(CreateWalkDetailCommand command) {
        WalkDetailEntity walkDetail = WalkDetailEntity.from(command);
        walkDetailRepository.save(walkDetail);
    }

    public void updateWalkToPet(List<UpdateWalkToPetRecordsCommand> commandList) {
        commandList.forEach(command -> command.getWalkToPet().updateRecord(command));
    }

    public void createEvent(CreateWalkPetEventCommand command) {
        WalkPetEventEntity walkPetEvent = WalkPetEventEntity.from(command);
        walkPetEventRepository.save(walkPetEvent);
    }

    public void createWalkToResource(CreateWalkToResourceCommand command) {
        WalkToResourceEntity walkToResource = WalkToResourceEntity.from(command);
        walkToResourceRepository.save(walkToResource);
    }

    public void upsertWalkToDetail(UpsertWalkToResourceCommand command) {
        walkToResourceRepository.deleteAll(command.getExistWalkToResourceList());
        List<CreateWalkToResourceCommand> commandList = CreateWalkToResourceCommand.from(command);
        List<WalkToResourceEntity> walkToResourceList = commandList.stream()
                .map(WalkToResourceEntity::from)
                .collect(Collectors.toList());
        walkToResourceRepository.saveAll(walkToResourceList);
    }

    public void updateWalkDetail(WalkDetailEntity walkDetail, UpdateWalkDetailCommand updateWalkDetailCommand) {
        walkDetail.update(updateWalkDetailCommand);
    }
}
