package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.module.walk.domain.entity.WalkResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkResourceRepository extends JpaRepository<WalkResourceEntity, Long> {
    List<WalkResourceEntity> findByIdIn(List<Long> resourceIds);
}
