package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.module.walk.domain.entity.WalkDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkDetailRepository extends JpaRepository<WalkDetailEntity, Long> {
}
