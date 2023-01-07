package com.onthe7.pupsroad.common.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onthe7.pupsroad.common.config.AppProperties;
import com.onthe7.pupsroad.common.exception.TokenException.TokenExpiredException;
import com.onthe7.pupsroad.common.exception.TokenException.TokenInvalidException;
import com.onthe7.pupsroad.common.exception.UserException;
import com.onthe7.pupsroad.common.security.domain.JwtUserDetails;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.UserCredentialDto;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserRefreshToken;
import com.onthe7.pupsroad.module.user.domain.vo.AuthVo.ReissueTokenVo;
import com.onthe7.pupsroad.module.user.infrastructure.AuthPrivateRepository;
import com.onthe7.pupsroad.module.user.infrastructure.UserRefreshTokenRepository;
import com.onthe7.pupsroad.module.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class SecurityUserService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUserService.class);

    private final AppProperties appProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;
    private final AuthPrivateRepository authPrivateRepository;

    Optional<UserCredentialDto> getUserCredentialByPrincipalId(String principalId) {
        return userRepository.getUserCredential(principalId);
    }

    // todo : Caching 처리
    UserEntity getUserByPrincipalId(String principalId) {
        return authPrivateRepository.findWithUserByPrincipalId(principalId)
                .orElseThrow(UserException.UserNotFoundException::new)
                .getUser();
    }

    String getToken(String principalId) {
        Instant now = Instant.now();
        Instant expiry = Instant.now().plus(Duration.ofMillis(appProperties.getTokenExpiry()));
        return JWT.create().withIssuer(appProperties.getTokenIssuer()).withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiry)).withSubject(principalId).sign(algorithm);
    }

    UserRefreshToken getRefreshTokenByPrincipalId(String principalId) {
        return userRefreshTokenRepository.findById(principalId)
                .orElseThrow(NoSuchElementException::new);
    }

    ReissueTokenVo reissueRefreshToken(String refreshToken, UserCredentialDto userCredential) {
        DecodedJWT decodedJwt = getDecodedToken(refreshToken).orElseThrow(TokenExpiredException::new);

        if (decodedJwt.getExpiresAt().before(new Date())) {
            String accessToken = getToken(userCredential.getPrincipalId());
            return new ReissueTokenVo(accessToken, getRefreshToken(userCredential.getPrincipalId()));
        }
        return new ReissueTokenVo(getToken(userCredential.getPrincipalId()), refreshToken);
    }

    String getRefreshToken(String principalId) {
        Instant now = Instant.now();
        Instant expiry = Instant.now().plus(Duration.ofMillis(appProperties.getRefreshTokenExpiry()));
        String token = JWT.create().withIssuer(appProperties.getTokenIssuer())
                .withIssuedAt(Date.from(now)).withExpiresAt(Date.from(expiry))
                .withSubject(appProperties.getTokenSecret()).sign(algorithm);

        saveUserRefreshToken(principalId, token);

        return token;
    }

    JwtUserDetails getUserDetails(UserCredentialDto userCredentialDto, String accessToken) {

        return JwtUserDetails.builder().username(userCredentialDto.getPrincipalId())
                .password(userCredentialDto.getPrincipalId())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(userCredentialDto.getRole().name())))
                .token(accessToken).build();
    }

    Optional<DecodedJWT> getDecodedToken(String token) {
        try {
            Optional<DecodedJWT> decodedJWT = Optional.of(verifier.verify(token));
            logger.debug("getDecodedToken() -> decoded subject : {}", decodedJWT.get().getSubject());
            return decodedJWT;
        } catch(JWTDecodeException ex) {
            throw new TokenInvalidException();
        } catch(JWTVerificationException ex) {
            throw new TokenExpiredException();
        }
    }

    public String getPrincipalIdBySecurityContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void saveUserRefreshToken(String principalId, String token) {
        UserRefreshToken refreshToken = UserRefreshToken.builder().principalId(principalId)
                .refreshToken(token).build();

        userRefreshTokenRepository.deleteById(refreshToken.getPrincipalId());
        userRefreshTokenRepository.save(refreshToken);
    }
}
