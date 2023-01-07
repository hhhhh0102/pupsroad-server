package com.onthe7.pupsroad.module.pet.infrastructure;

import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<PetEntity, Long>, PetCustomRepository {

    @Query("SELECT pet FROM PetEntity pet join pet.user where pet.id =:id")
    Optional<PetEntity> findFetchJoinedPetById(Long id);

    List<PetEntity> findAllByIdIn(List<Long> petIds);

    List<PetEntity> findAllByUserAndIdInAndDeleted(UserEntity user, List<Long> petIds, YesNo deleted);
}
