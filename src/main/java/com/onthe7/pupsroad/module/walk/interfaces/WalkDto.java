package com.onthe7.pupsroad.module.walk.interfaces;

import com.onthe7.pupsroad.module.walk.domain.enums.WalkEventType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class WalkDto {

    @Getter
    @Setter
    public static class StartWalkRequest {
        @NotEmpty(message = "선택한 반려견이 없습니다")
        private List<Long> petIds;
        @NotNull(message = "출발 경도 정보가 필요합니다")
        private Double startLongitude;
        @NotNull(message = "출발 위도 정보가 필요합니다")
        private Double startLatitude;
    }

    @Getter
    @Setter
    public static class CreateWalkRouteRequest {
        @NotBlank(message = "경로 정보가 필요합니다")
        private String route;
    }

    @Getter
    @Setter
    public static class CreateWalkRecordRequest {
        @NotNull(message = "산책 시작 시간 값이 필요합니다")
        private LocalDateTime startTime;
        @NotNull(message = "산책 시작 시간 값이 필요합니다")
        private LocalDateTime endTime;
        @NotNull(message = "총 산책 시간 값이 필요합니다")
        private Integer totalTime;
        @NotNull(message = "총 산책 거리 값이 필요합니다")
        private Double totalDistance;
        // todo : error message 정하기
        @NotEmpty(message = "")
        private List<PetWalkRecord> petWalkRecords;
        private String memo;

        @Getter
        @Setter
        public static class PetWalkRecord {
            @NotNull(message = "반려견 정보가 필요합니다")
            Long petId;
            @NotNull(message = "반려견이 소바한 칼로리 정보가 필요합니다")
            Double calorie;
        }
    }

    @Getter
    @Setter
    public static class CreateWalkResourceRequest {
        @NotNull(message = "파일 정보가 필요합니다")
        private Long resourceId;
    }

    @Getter
    @Setter
    public static class CreateWalkEventRequest {
        private Long petId;
        private WalkEventType eventType;
        private LocalDateTime eventTime;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateWalkResponse {
        private Long walkId;

        public static CreateWalkResponse success(Long walkId) {
            return new CreateWalkResponse(walkId);
        }

    }

    @Getter
    @Setter
    public static class UpdateWalkDetail {
        private String memo;
        private List<Long> resourceIds;
    }

}
