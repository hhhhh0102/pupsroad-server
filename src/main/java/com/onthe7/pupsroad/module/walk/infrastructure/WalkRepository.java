package com.onthe7.pupsroad.module.walk.infrastructure;

import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.walk.domain.entity.WalkEntity;
import com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalkRepository extends JpaRepository<WalkEntity, Long>, WalkCustomRepository {

    @Query("SELECT walk FROM WalkEntity walk left join fetch walk.walkToResourceList left join fetch walk.walkDetail where walk.id =:walkId")
    Optional<WalkEntity> findWalkWithWalkToResourceAndDetailById(Long walkId);

    Optional<WalkEntity> findWalkByUserAndStatus(UserEntity user, WalkStatus inProgress);
}
