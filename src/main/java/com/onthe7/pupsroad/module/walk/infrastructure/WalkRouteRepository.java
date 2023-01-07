package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.module.walk.domain.entity.WalkRouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRouteRepository extends JpaRepository<WalkRouteEntity, Long> {
}
