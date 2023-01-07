package com.onthe7.pupsroad.module.pet.domain.service;

import com.onthe7.pupsroad.module.pet.domain.dto.request.PetCommand;
import com.onthe7.pupsroad.module.pet.domain.dto.request.PetCommand.UpdatePetProfileCommand;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.pet.infrastructure.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetCommandService {

    private final PetRepository petRepository;

    public Long createPet(PetCommand.CreatePetProfileCommand command) {
        PetEntity pet = petRepository.save(PetEntity.from(command));
        return pet.getId();
    }

    public void updatePet(PetEntity pet, UpdatePetProfileCommand command) {
        pet.updatePetProfile(command);
    }

    public void deletePet(PetEntity pet, Long userId) {
        pet.deletePetProfile(userId);
    }
}
