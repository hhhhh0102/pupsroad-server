package com.onthe7.pupsroad.module.pet.interfaces;

import com.onthe7.pupsroad.module.pet.domain.enums.PetGender;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PetDto {

    @Getter
    @Setter
    public static class CreatePetProfileRequest {
        @NotNull(message = "반려견의 이름 정보가 필요합니다")
        private String name;
        @NotNull(message = "견종 정보가 필요합니다")
        private String breed;
        private LocalDateTime birthdate;
        private Double weight;
        @NotNull(message = "반려견의 성별 정보가 필요합니다")
        private PetGender gender;
        @NotNull(message = "중성화 여부 정보가 필요합니다")
        private Boolean neutralized;

        private Long resourceId;
    }

    @Getter
    @Setter
    public static class UpdatePetProfileRequest {
        @NotNull(message = "반려견의 이름 정보가 필요합니다")
        private String name;
        @NotNull(message = "견종 정보가 필요합니다")
        private String breed;
        private LocalDateTime birthdate;
        private Double weight;
        @NotNull(message = "반려견의 성별 정보가 필요합니다")
        private PetGender gender;
        @NotNull(message = "중성화 여부 정보가 필요합니다")
        private Boolean neutralized;

        private Long resourceId;
    }

    @Getter
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreatePetProfileResponse {
        private final Long petId;

        public static CreatePetProfileResponse success(Long petId) {
            return new CreatePetProfileResponse(petId);
        }
    }
}
