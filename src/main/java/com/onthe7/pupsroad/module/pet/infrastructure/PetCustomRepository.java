package com.onthe7.pupsroad.module.pet.infrastructure;

import com.onthe7.pupsroad.module.pet.domain.dto.response.PetQueryResponse.PetDetail;

import java.util.List;
import java.util.Optional;

public interface PetCustomRepository {

    Optional<PetDetail> getPetDetail(Long petId);
    List<PetDetail> getUserPetDetailList(Long userId);
}
