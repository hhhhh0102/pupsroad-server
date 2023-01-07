package com.onthe7.pupsroad.module.pet.domain.service;

import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.common.exception.PetException;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.module.pet.domain.dto.response.PetQueryResponse.PetDetail;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.pet.domain.entity.PetResourceEntity;
import com.onthe7.pupsroad.module.pet.infrastructure.PetRepository;
import com.onthe7.pupsroad.module.pet.infrastructure.PetResourceRepository;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetQueryService {

    private final PetRepository petRepository;
    private final PetResourceRepository petResourceRepository;

    public PetEntity getPetEntityById(Long petId) {
        return petRepository.findFetchJoinedPetById(petId)
                .orElseThrow(PetException.PetNotFoundException::new);
    }

    public List<PetDetail> getUserPetDetailList(Long userId) {
        return petRepository.getUserPetDetailList(userId);
    }

    public PetDetail getPetDetailById(Long petId) {
        return petRepository.getPetDetail(petId)
                .orElseThrow(PetException.PetNotFoundException::new);
    }

    public PetResourceEntity getPetResourceEntityById(Long resourceId) {
        return petResourceRepository.findById(resourceId)
                .orElseThrow(() -> new UIException(ErrorCode.CLIENT_INVALID_PARAM, "잘못된 리소스 정보를 입력하였습니다"));
    }

    public List<PetEntity> getUserPetEntityListInIds(UserEntity user, List<Long> petIds) {
        return petRepository.findAllByUserAndIdInAndDeleted(user, petIds, YesNo.N);
    }
}
