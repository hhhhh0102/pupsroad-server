package com.onthe7.pupsroad.module.user.domain.service;

import com.onthe7.pupsroad.common.exception.UserException;
import com.onthe7.pupsroad.common.vo.ResultVo;
import com.onthe7.pupsroad.module.gallery.infrastructure.GalleryRepository;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.UserSignUpDto;
import com.onthe7.pupsroad.module.user.domain.dto.UserDto;
import com.onthe7.pupsroad.module.user.domain.entity.AuthPrivateEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.user.domain.vo.UserVo;
import com.onthe7.pupsroad.module.user.infrastructure.AuthPrivateRepository;
import com.onthe7.pupsroad.module.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final AuthPrivateRepository authPrivateRepository;
    private final GalleryRepository galleryRepository;

    /**
     * 유저 회원 가입
     */
    @Transactional(rollbackOn = Exception.class)
    public String createUser(UserSignUpDto userSignUpDto) {
        if (authPrivateRepository.findByPrincipalIdAndProviderAndDeletedAtIsNull(userSignUpDto.getPrincipalId(), userSignUpDto.getProvider()).isPresent()) {
            throw new UserException.UserAlreadySignedUpException();
        }

        if (userRepository.findByNicknameAndDeletedAtIsNull(userSignUpDto.getNickname()).isPresent()) {
            throw new UserException.DuplicateNicknameException();
        }

        UserEntity user = UserEntity.createUser(userSignUpDto);
        AuthPrivateEntity authPrivate = AuthPrivateEntity.createOAuthPrivateEntity(userSignUpDto, user);
        userRepository.save(user);
        authPrivateRepository.save(authPrivate);
        return authPrivate.getPrincipalId();
    }


    /**
     * 유저정보 조회
     */
    @Transactional(rollbackOn = Exception.class)
    public UserVo.UserInfoVo showUser(String cliendId) {
        UserEntity user = userRepository.findByClientIdAndDeletedAtIsNull(cliendId).orElseThrow(UserException.UserNotFoundException::new);
        AuthPrivateEntity authPrivate = authPrivateRepository.findByUser_IdAndDeletedAtIsNull(user.getId()).orElseThrow(UserException.UserNotFoundException::new);

        return UserVo.UserInfoVo.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .phone(authPrivate.getPhone())
                .email(authPrivate.getEmail())
                .provider(authPrivate.getProvider().getName())
//                .resource(user.getResource)
                .datetime(user.getCreatedAt())
                .build();
    }


    /**
     * 유저정보 수정
     */
    @Transactional(rollbackOn = Exception.class)
    public ResultVo editUser(UserDto.UserInfoDto userInfoDto, String cliendId) {
        UserEntity user = userRepository.findByClientIdAndDeletedAtIsNull(cliendId).orElseThrow(UserException.UserNotFoundException::new);
        AuthPrivateEntity authPrivate = authPrivateRepository.findByUser_IdAndDeletedAtIsNull(user.getId()).orElseThrow(UserException.UserNotFoundException::new);

        user.setNickname(userInfoDto.getNickname());
//        user.setResource(userInfoDto.getResource());
        authPrivate.setPhone(userInfoDto.getPhone());
        authPrivate.setEmail(userInfoDto.getEmail());

        userRepository.save(user);
        authPrivateRepository.save(authPrivate);

        return new ResultVo(true);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional(rollbackOn = Exception.class)
    public ResultVo deleteUser(String cliendId) {
        UserEntity user = userRepository.findByClientIdAndDeletedAtIsNull(cliendId).orElseThrow(UserException.UserNotFoundException::new);
        AuthPrivateEntity authPrivate = authPrivateRepository.findByUser_IdAndDeletedAtIsNull(user.getId()).orElseThrow(UserException.UserNotFoundException::new);

        user.deleteUser(user.getId());
        authPrivate.deleteOAuthPrivateEntity(user.getId());

        return new ResultVo(true);
    }

}
