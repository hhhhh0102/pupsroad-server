package com.onthe7.pupsroad.common.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onthe7.pupsroad.common.exception.UserException;
import com.onthe7.pupsroad.common.exception.UserException.UserNotFoundException;
import com.onthe7.pupsroad.common.security.domain.JwtUserDetails;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.ReissueTokenDto;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.UserCredentialDto;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserRefreshToken;
import com.onthe7.pupsroad.module.user.domain.vo.AuthVo.ReissueTokenVo;
import com.onthe7.pupsroad.module.user.domain.vo.AuthVo.UserLoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityUserFacade {

    private final SecurityUserService securityUserService;

    public UserEntity getLoginUser() {
        String principalId = securityUserService.getPrincipalIdBySecurityContext();
        return securityUserService.getUserByPrincipalId(principalId);
    }

    public UserCredentialDto getLoginUserCredential() {
        String principalId = securityUserService.getPrincipalIdBySecurityContext();
        return securityUserService.getUserCredentialByPrincipalId(principalId)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserCredentialDto getUserCredential(String principalId) {
        return securityUserService.getUserCredentialByPrincipalId(principalId)
                .orElseThrow(UserNotFoundException::new);
    }

    public ReissueTokenVo reissue(ReissueTokenDto input) {
        String principalId = JWT.decode(input.getAccessToken()).getSubject();
        UserCredentialDto userCredential = securityUserService.getUserCredentialByPrincipalId(principalId)
                .orElseThrow(UserNotFoundException::new);

        UserRefreshToken redisToken = securityUserService.getRefreshTokenByPrincipalId(userCredential.getPrincipalId());

        if (redisToken.getRefreshToken().equals(input.getRefreshToken())) {
            return securityUserService.reissueRefreshToken(redisToken.getRefreshToken(), userCredential);
        }

        throw new IllegalArgumentException();
    }

    public void setUserAuthentication(UserCredentialDto userCredential) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        userCredential.getPrincipalId(), null, List.of(userCredential.getRole())));
    }

    public UserLoginVo getLoginInfoVo(String principalId) {
        UserCredentialDto userCredential = getUserCredential(principalId);
        String accessToken = securityUserService.getToken(principalId);
        String refreshToken = securityUserService.getRefreshToken(principalId);
        return new UserLoginVo(accessToken, refreshToken, userCredential.getClientId());
    }

    // todo : Caching 처리
    public JwtUserDetails loadUserByToken(String token) {
        return securityUserService.getDecodedToken(token)
                .map(DecodedJWT::getSubject)
                .map(securityUserService::getUserCredentialByPrincipalId)
                .orElseThrow(UserException.UserNotFoundException::new)
                .map(userCredentialDto -> securityUserService.getUserDetails(userCredentialDto, token))
                .orElse(null);
    }
}
