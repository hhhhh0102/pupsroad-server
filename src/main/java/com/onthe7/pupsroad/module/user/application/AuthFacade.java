package com.onthe7.pupsroad.module.user.application;

import com.onthe7.pupsroad.common.exception.UserException;
import com.onthe7.pupsroad.common.security.service.SecurityUserFacade;
import com.onthe7.pupsroad.common.vo.ResultVo;
import com.onthe7.pupsroad.module.gallery.domain.service.GalleryCommandService;
import com.onthe7.pupsroad.module.otp.application.OtpService;
import com.onthe7.pupsroad.module.otp.domain.dto.OtpDto;
import com.onthe7.pupsroad.module.otp.domain.vo.OtpVo;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.user.domain.service.UserCommandService;
import com.onthe7.pupsroad.module.user.domain.vo.AuthVo.ReissueTokenVo;
import com.onthe7.pupsroad.module.user.domain.vo.AuthVo.UserLoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.onthe7.pupsroad.module.user.domain.dto.AuthDto.*;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final SecurityUserFacade userFacade;
    private final UserCommandService userCommandService;
    private final GalleryCommandService galleryCommandService;
    private final OtpService otpService;

    public UserLoginVo signup(UserSignUpDto userSignUpDto) {
        String principalId = userCommandService.createUser(userSignUpDto);
        UserCredentialDto userCredential = userFacade.getUserCredential(principalId);
        userFacade.setUserAuthentication(userCredential);
        UserEntity user = userFacade.getLoginUser();
        galleryCommandService.createUserDefaultGallery(user.getId(), user.getNickname());
        return userFacade.getLoginInfoVo(principalId);
    }

    public UserLoginVo login(UserLoginDto userLoginDto) {
        UserCredentialDto userCredential = userFacade.getUserCredential(userLoginDto.getPrincipalId());
        userFacade.setUserAuthentication(userCredential);
        return userFacade.getLoginInfoVo(userCredential.getPrincipalId());
    }

    public ReissueTokenVo reissueToken(ReissueTokenDto reissueTokenDto) {
        return userFacade.reissue(reissueTokenDto);
    }


    /**
     * 인증번호 발송
     */
    public ResultVo sendOtp(OtpDto.SendOtpDto sendOtpDto) {
        OtpVo.OtpInfo otpInfo = otpService.sendOtp(sendOtpDto);

        if (otpInfo == null) {
            throw new UserException.OtpSendFailedException();
        }
        return new ResultVo(true);
    }

    /**
     * 인증번호 확인
     */
    @Transactional
    public ResultVo checkOtp(OtpDto.CheckOtpDto checkOtpDto) {
        // OTP 일치여부 체크
        boolean isVerified = otpService.checkOtp(checkOtpDto);

        if (isVerified == true) {
            return new ResultVo(true);
        }

        return null;
    }

}
