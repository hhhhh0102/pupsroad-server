package com.onthe7.pupsroad.module.pet.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.module.pet.domain.enums.PetGender;
import com.onthe7.pupsroad.module.resource.domain.enums.ResourceType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class PetQueryResponse {

    @Getter
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class PetDetail {
        private final Long id;
        private final String name;
        private final String breed;
        private final LocalDateTime birthdate;
        private final Double weight;
        private final PetGender gender;
        private final Boolean neutralized;
        private final PetResourceInfo resourceInfo;

        @QueryProjection
        public PetDetail(Long id, String name, String breed, LocalDateTime birthdate,
                         Double weight, PetGender gender, YesNo neutralized,
                         PetResourceInfo resourceInfo) {
            this.id = id;
            this.name = name;
            this.breed = breed;
            this.birthdate = birthdate;
            this.weight = weight;
            this.gender = gender;
            this.neutralized = neutralized.equals(YesNo.Y) ? Boolean.TRUE: Boolean.FALSE;
            this.resourceInfo = resourceInfo;
        }
    }

    @Getter
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class PetResourceInfo {
        private final Long id;
        private final ResourceType type;
        private final String url;
        private final String originalFilename;

        @QueryProjection
        public PetResourceInfo(Long id, ResourceType type, String url, String originalFilename) {
            this.id = id;
            this.type = type;
            this.url = url;
            this.originalFilename = originalFilename;
        }
    }
}
