package com.onthe7.pupsroad.module.walk.domain.service;

import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkToPetEntity;
import com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.onthe7.pupsroad.common.exception.WalkException.WalkIsNotFinishedException;
import static com.onthe7.pupsroad.common.exception.WalkException.WalkIsNotInProgressException;
import static com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus.FINISHED;
import static com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus.IN_PROGRESS;

@Service
@RequiredArgsConstructor
public class WalkValidationService {

    public void validateUpdateWalkToPetCommand(List<PetEntity> petList, List<WalkToPetEntity> walkToPetList) {

        walkToPetList.forEach(walkToPet -> petList.stream().map(PetEntity::getId)
                .filter(petId -> petId.equals(walkToPet.getPet().getId()))
                .findAny()
                .orElseThrow(() -> new UIException(ErrorCode.CLIENT_INVALID_PARAM,
                        String.format("petId: %s 의 산책 정보가 누락되었습니다", walkToPet.getPet().getId()))
                ));

        petList.forEach(pet -> walkToPetList.stream()
                .map(WalkToPetEntity::getPet)
                .map(PetEntity::getId)
                .filter(petId -> petId.equals(pet.getId()))
                .findAny()
                .orElseThrow(() -> new UIException(ErrorCode.CLIENT_INVALID_PARAM,
                        String.format("같이 산책하지 않은 반려견이 존재합니다. petId: %s", pet.getId())
                ))
        );
    }

    public void userHasWalkCommandPriority(WalkEntity walk, UserEntity user) {
        if (!walk.getCreatedBy().equals(user.getId())) {
            throw new UIException(ErrorCode.NO_PERMISSION, "");
        }
    }

    public void eqWalkStatusOrThrow(WalkEntity walk, WalkStatus status) {
        switch (status) {
            case IN_PROGRESS:
                if (!walk.getStatus().equals(IN_PROGRESS)) {
                    throw new WalkIsNotInProgressException();
                }
                break;
            case FINISHED:
                if (!walk.getStatus().equals(FINISHED)) {
                    throw new WalkIsNotFinishedException();
                }
                break;
        }
    }
}
