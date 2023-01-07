package com.onthe7.pupsroad.module.walk.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onthe7.pupsroad.common.domain.dto.Cursor;
import com.onthe7.pupsroad.module.resource.domain.dto.response.ResourceQueryResponse.ResourceElement;
import com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class WalkQueryResponse {

    @Getter
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class PetWalkElement implements Cursor {
        private final String petName;
        private final LocalDateTime eventDate;
        private final WalkStatus status;
        private final Integer totalTime;
        private final Double totalDistance;
        private final Double calorie;

        @JsonIgnore
        private final String cursor;

        @QueryProjection
        public PetWalkElement(String petName, LocalDateTime eventDate,
                              WalkStatus status, Integer totalTime,
                              Double totalDistance, Double calorie,
                              String cursor) {
            this.petName = petName;
            this.eventDate = eventDate;
            this.status = status;
            this.totalTime = totalTime;
            this.totalDistance = totalDistance;
            this.calorie = calorie;
            this.cursor = cursor;
        }

        @QueryProjection
        public PetWalkElement(String petName, LocalDateTime eventDate,
                              WalkStatus status, Integer totalTime,
                              Double totalDistance, Double calorie) {
            this.petName = petName;
            this.eventDate = eventDate;
            this.status = status;
            this.totalTime = totalTime;
            this.totalDistance = totalDistance;
            this.calorie = calorie;
            this.cursor = null;
        }
    }

    @Getter
    @Builder
    public static class WalkLocationInfo {
        private final Long walkId;
        private final Double latitude;
        private final Double longitude;
    }

    @Getter
    public static class WalkDetail {
        private final Long walkId;
        private final String route;
        private final String memo;
        private final Long userId;

        @Setter
        private List<PetWalkElement> petWalkList;
        @Setter
        private List<ResourceElement> resourceList;

        @QueryProjection
        public WalkDetail(Long walkId, String route,
                          String memo, Long userId) {
            this.walkId = walkId;
            this.route = route;
            this.memo = memo;
            this.userId = userId;
        }
    }
}
