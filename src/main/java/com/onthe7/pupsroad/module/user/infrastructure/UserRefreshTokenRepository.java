package com.onthe7.pupsroad.module.user.infrastructure;

import com.onthe7.pupsroad.module.user.domain.entity.UserRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface UserRefreshTokenRepository extends CrudRepository<UserRefreshToken, String> {
}
