package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.module.walk.domain.entity.WalkToResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkToResourceRepository extends JpaRepository<WalkToResourceEntity, Long> {
}
