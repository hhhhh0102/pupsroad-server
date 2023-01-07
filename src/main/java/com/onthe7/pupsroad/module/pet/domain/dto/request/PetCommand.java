package com.onthe7.pupsroad.module.pet.domain.dto.request;

import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.pet.domain.entity.PetResourceEntity;
import com.onthe7.pupsroad.module.pet.domain.enums.PetGender;
import com.onthe7.pupsroad.module.pet.interfaces.PetDto.CreatePetProfileRequest;
import com.onthe7.pupsroad.module.pet.interfaces.PetDto.UpdatePetProfileRequest;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

public class PetCommand {

    @Getter
    @Builder(access = PRIVATE)
    public static class CreatePetProfileCommand {
        private final UserEntity user;
        private final String name;
        private final String breed;
        private final LocalDateTime birthdate;
        private final Double weight;
        private final PetGender gender;
        private final YesNo neutralized;
        private final PetResourceEntity petResource;

        public static CreatePetProfileCommand toCommand(CreatePetProfileRequest request,
                                                        UserEntity user,
                                                        PetResourceEntity petResource) {
            return CreatePetProfileCommand.builder()
                    .user(user)
                    .name(request.getName()).breed(request.getBreed())
                    .birthdate(request.getBirthdate()).weight(request.getWeight())
                    .gender(request.getGender())
                    .neutralized(request.getNeutralized() ? YesNo.Y: YesNo.N)
                    .petResource(petResource)
                    .build();
        }
    }

    @Getter
    @Builder(access = PRIVATE)
    public static class UpdatePetProfileCommand {
        private final UserEntity user;
        private final String name;
        private final String breed;
        private final LocalDateTime birthdate;
        private final Double weight;
        private final PetGender gender;
        private final YesNo neutralized;
        private final PetResourceEntity petResource;

        public static UpdatePetProfileCommand toCommand(UpdatePetProfileRequest request,
                                                        UserEntity user,
                                                        PetResourceEntity petResource) {
            return UpdatePetProfileCommand.builder()
                    .user(user)
                    .name(request.getName()).breed(request.getBreed())
                    .birthdate(request.getBirthdate()).weight(request.getWeight())
                    .gender(request.getGender())
                    .neutralized(request.getNeutralized() ? YesNo.Y: YesNo.N)
                    .petResource(petResource)
                    .build();
        }
    }
}
