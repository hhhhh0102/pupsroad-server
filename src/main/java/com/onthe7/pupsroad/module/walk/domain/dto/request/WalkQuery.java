package com.onthe7.pupsroad.module.walk.domain.dto.request;

import com.onthe7.pupsroad.common.domain.dto.PaginationDto.PaginationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class WalkQuery {

    @Getter
    @AllArgsConstructor
    public static class PetWalkElementList {
        private final Long petId;
        private final Integer year;
        private final Integer month;
        private final PaginationRequestDto paginationRequest;
    }

    @Getter
    @AllArgsConstructor
    public static class WalkLocationInfoList {
        private final Double latitude;
        private final Double longitude;
        private final Double distance;
        private final Long offset;
        private final Long limit;
    }

    @Getter
    @AllArgsConstructor
    public static class PetToWalkEntity {
        private final Long walkId;
        private final Long petId;
    }
}
