package com.onthe7.pupsroad.module.pet.appilcation;

import com.onthe7.pupsroad.common.security.service.SecurityUserFacade;
import com.onthe7.pupsroad.common.vo.ResultVo;
import com.onthe7.pupsroad.module.pet.domain.dto.response.PetQueryResponse.PetDetail;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.pet.domain.entity.PetResourceEntity;
import com.onthe7.pupsroad.module.pet.domain.service.PetCommandService;
import com.onthe7.pupsroad.module.pet.domain.service.PetQueryService;
import com.onthe7.pupsroad.module.pet.domain.service.PetValidationService;
import com.onthe7.pupsroad.module.pet.interfaces.PetDto;
import com.onthe7.pupsroad.module.pet.interfaces.PetDto.UpdatePetProfileRequest;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.onthe7.pupsroad.module.pet.domain.dto.request.PetCommand.CreatePetProfileCommand;
import static com.onthe7.pupsroad.module.pet.domain.dto.request.PetCommand.UpdatePetProfileCommand;

@Service
@RequiredArgsConstructor
public class PetFacade {

    private final SecurityUserFacade userFacade;
    private final PetCommandService petCommandService;
    private final PetQueryService petQueryService;
    private final PetValidationService petValidationService;

    @Transactional
    public Long createPetProfile(PetDto.CreatePetProfileRequest request) {
        UserEntity user = userFacade.getLoginUser();
        PetResourceEntity petResource = Objects.nonNull(request.getResourceId()) ?
                petQueryService.getPetResourceEntityById(request.getResourceId()) : null;
        CreatePetProfileCommand command = CreatePetProfileCommand.toCommand(request, user, petResource);
        return petCommandService.createPet(command);
    }

    @Transactional
    public ResultVo updatePetProfile(Long petId, UpdatePetProfileRequest request) {
        UserEntity user = userFacade.getLoginUser();
        PetEntity pet = petQueryService.getPetEntityById(petId);
        PetResourceEntity petResource = Objects.nonNull(request.getResourceId()) ?
                petQueryService.getPetResourceEntityById(request.getResourceId()) : null;
        petValidationService.hasPetCommandPermissionOrThrow(user, pet);
        UpdatePetProfileCommand command = UpdatePetProfileCommand.toCommand(request, user, petResource);
        petCommandService.updatePet(pet, command);
        return new ResultVo(true);
    }

    @Transactional
    public ResultVo deletePetProfile(Long petId) {
        UserEntity user = userFacade.getLoginUser();
        PetEntity pet = petQueryService.getPetEntityById(petId);
        petValidationService.hasPetCommandPermissionOrThrow(user, pet);
        petCommandService.deletePet(pet, user.getId());
        return new ResultVo(true);
    }

    public List<PetDetail> getUserPetDetailList() {
        Long userId = userFacade.getLoginUser().getId();
        return petQueryService.getUserPetDetailList(userId);
    }

    public PetDetail getPetDetail(Long petId) {
        return petQueryService.getPetDetailById(petId);
    }
}
