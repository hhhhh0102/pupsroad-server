package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.module.walk.domain.entity.WalkPetEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkPetEventRepository extends JpaRepository<WalkPetEventEntity, Long> {
}
