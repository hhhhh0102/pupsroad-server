package com.onthe7.pupsroad.module.pet.domain.service;

import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetValidationService {

    public void hasPetCommandPermissionOrThrow(UserEntity user, PetEntity pet) {
        Long userId = user.getId();
        Long petOwnerId = pet.getUser().getId();
        if(!userId.equals(petOwnerId)) {
            throw new UIException(ErrorCode.NO_PERMISSION, "반려견을 수정할 권한이 없습니다.");
        }
    }
}
