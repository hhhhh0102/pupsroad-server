package com.onthe7.pupsroad.module.walk.domain.dto.request;

import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkResourceEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkToPetEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkToResourceEntity;
import com.onthe7.pupsroad.module.walk.domain.enums.WalkEventType;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.CreateWalkRecordRequest;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.CreateWalkRecordRequest.PetWalkRecord;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.CreateWalkRouteRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

public class WalkCommand {

    @Getter
    @AllArgsConstructor(access = PRIVATE)
    public static class CreateWalkCommand {
        private final UserEntity user;
        private final List<PetEntity> petList;
        private final Double startLongitude;
        private final Double startLatitude;


        public static CreateWalkCommand toCommand(UserEntity user, List<PetEntity> petList,
                                                  WalkDto.StartWalkRequest request) {
            return new CreateWalkCommand(user, petList,
                    request.getStartLongitude(), request.getStartLatitude());
        }
    }

    @Getter
    @AllArgsConstructor(access = PRIVATE)
    public static class CreateWalkToPetCommand {
        private final WalkEntity walk;
        private final List<PetEntity> petList;
        private final UserEntity user;

        public static CreateWalkToPetCommand toCommand(CreateWalkCommand createWalkCommand, WalkEntity walk) {
            return new CreateWalkToPetCommand(walk, createWalkCommand.petList, createWalkCommand.getUser());
        }
    }

    @Getter
    @AllArgsConstructor(access = PRIVATE)
    public static class CreateWalkToResourceCommand {
        private final WalkEntity walk;
        private final WalkResourceEntity resource;
        private final UserEntity user;

        public static CreateWalkToResourceCommand toCommand(WalkEntity walk, WalkResourceEntity resource,
                                                       UserEntity user) {
            return new CreateWalkToResourceCommand(walk, resource, user);
        }

        public static List<CreateWalkToResourceCommand> from(UpsertWalkToResourceCommand command) {
            UserEntity user = command.getUser();
            WalkEntity walk = command.getWalk();
            return command.getNewResourceList().stream()
                    .map(newResource -> new CreateWalkToResourceCommand(walk, newResource, user))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor(access = PRIVATE)
    public static class CreateWalkRouteCommand {
        private final WalkEntity walk;
        private final UserEntity user;
        private final String route;

        public static CreateWalkRouteCommand toCommand(WalkEntity walk, UserEntity user,
                                                       CreateWalkRouteRequest request) {
            return new CreateWalkRouteCommand(walk, user, request.getRoute());
        }
    }

    @Getter
    @AllArgsConstructor(access = PRIVATE)
    public static class CreateWalkPetEventCommand {
        private final WalkToPetEntity walkToPet;
        private final WalkEventType type;
        private final LocalDateTime eventTime;
        private final Long userId;

        public static CreateWalkPetEventCommand toCommand(WalkToPetEntity walkToPet, Long userId,
                                                          WalkDto.CreateWalkEventRequest request) {
            return new CreateWalkPetEventCommand(walkToPet,
                    request.getEventType(), request.getEventTime(), userId);
        }
    }

    @Getter
    @Builder(access = PRIVATE)
    public static class CreateWalkDetailCommand {
        private final WalkEntity walk;
        private final UserEntity user;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final Integer totalTime;
        private final Double totalDistance;
        private final String memo;

        public static CreateWalkDetailCommand toCommand(WalkEntity walk, UserEntity user,
                                                        CreateWalkRecordRequest request) {
            return CreateWalkDetailCommand
                    .builder().walk(walk).user(user)
                    .startTime(request.getStartTime()).endTime(request.getEndTime())
                    .totalTime(request.getTotalTime()).totalDistance(request.getTotalDistance())
                    .memo(request.getMemo()).build();
        }
    }

    @Getter
    @Builder(access = PRIVATE)
    @AllArgsConstructor(access = PRIVATE)
    public static class UpdateWalkToPetRecordsCommand {
        WalkToPetEntity walkToPet;
        Double calorie;

        public static UpdateWalkToPetRecordsCommand toCommand(WalkToPetEntity walkToPet, PetWalkRecord record) {
            return UpdateWalkToPetRecordsCommand
                    .builder()
                    .walkToPet(walkToPet).calorie(record.getCalorie())
                    .build();
        }

        public static List<UpdateWalkToPetRecordsCommand> toCommandList(List<WalkToPetEntity> walkToPetList,
                                                                        CreateWalkRecordRequest request) {
            return walkToPetList.stream().map(walkToPet -> request.getPetWalkRecords().stream()
                    .filter(petWalkRecord -> petWalkRecord.getPetId().equals(walkToPet.getPet().getId()))
                    .findFirst()
                    .map(petWalkRecord -> UpdateWalkToPetRecordsCommand.toCommand(walkToPet, petWalkRecord))
                    .orElseThrow(InvalidParameterException::new)
            ).collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor(access = PRIVATE)
    public static class UpsertWalkToResourceCommand {
        private final WalkEntity walk;
        private final List<WalkToResourceEntity> existWalkToResourceList;
        private final List<WalkResourceEntity> newResourceList;
        private final UserEntity user;

        public static UpsertWalkToResourceCommand toCommand(WalkEntity walk, UserEntity user,
                                                            List<WalkResourceEntity> newResourceList) {
            return new UpsertWalkToResourceCommand(walk, walk.getWalkToResourceList(), newResourceList, user);
        }
    }

    @Getter
    @AllArgsConstructor(access = PRIVATE)
    public static class UpdateWalkDetailCommand {
        private final String memo;
        private final UserEntity user;

        public static UpdateWalkDetailCommand toCommand(WalkDto.UpdateWalkDetail request,
                                                        UserEntity user) {
            return new UpdateWalkDetailCommand(request.getMemo(), user);
        }
    }
}
