package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.module.walk.domain.entity.WalkEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkToPetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalkToPetRepository extends JpaRepository<WalkToPetEntity, Long> {
    List<WalkToPetEntity> findAllByWalk(WalkEntity walk);
}
