package com.onthe7.pupsroad.module.pet.infrastructure;

import com.onthe7.pupsroad.module.pet.domain.entity.PetResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetResourceRepository extends JpaRepository<PetResourceEntity, Long> {
}
