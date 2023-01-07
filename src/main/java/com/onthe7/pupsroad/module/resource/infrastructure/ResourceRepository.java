package com.onthe7.pupsroad.module.resource.infrastructure;

import com.onthe7.pupsroad.module.resource.domain.entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
}
