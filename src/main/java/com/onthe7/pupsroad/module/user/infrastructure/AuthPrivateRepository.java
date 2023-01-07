package com.onthe7.pupsroad.module.user.infrastructure;

import com.onthe7.pupsroad.common.enums.AuthProviderType;
import com.onthe7.pupsroad.module.user.domain.entity.AuthPrivateEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthPrivateRepository extends JpaRepository<AuthPrivateEntity, Long> {

    Optional<AuthPrivateEntity> findByEmail(String email);

    Optional<AuthPrivateEntity> findByEmailAndProvider(String email, AuthProviderType provider);

    Optional<AuthPrivateEntity> findByPrincipalIdAndProviderAndDeletedAtIsNull(String principalId, AuthProviderType provider);

    Optional<AuthPrivateEntity> findByUserAndProvider(UserEntity user, AuthProviderType provider);

    Optional<AuthPrivateEntity> findByUser_IdAndDeletedAtIsNull(Long userId);

    @Query("SELECT authPrivate FROM AuthPrivateEntity authPrivate join authPrivate.user where authPrivate.principalId =:principalId")
    Optional<AuthPrivateEntity> findWithUserByPrincipalId(String principalId);
}
