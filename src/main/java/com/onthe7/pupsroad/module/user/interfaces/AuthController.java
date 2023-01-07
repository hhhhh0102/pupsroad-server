package com.onthe7.pupsroad.module.user.interfaces;

import com.onthe7.pupsroad.common.util.RequestIdGenerator;
import com.onthe7.pupsroad.common.vo.Response;
import com.onthe7.pupsroad.common.vo.ResultVo;
import com.onthe7.pupsroad.module.otp.domain.dto.OtpDto;
import com.onthe7.pupsroad.module.user.application.AuthFacade;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.ReissueTokenDto;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.UserLoginDto;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.UserSignUpDto;
import com.onthe7.pupsroad.module.user.domain.vo.AuthVo.UserLoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.onthe7.pupsroad.module.user.domain.vo.AuthVo.ReissueTokenVo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RequestIdGenerator requestIdGenerator;
    private final AuthFacade authFacade;

    /**
     * 사용자 회원가입 API
     */
    @PostMapping("/signup")
    public Response<UserLoginVo> signUp(@RequestBody UserSignUpDto userSignUpDto) {
        String requestId = requestIdGenerator.getRequestId();
        UserLoginVo loginVo = authFacade.signup(userSignUpDto);
        return Response.success(requestId, loginVo);
    }

    /**
     * 인증번호 발송 API
     */
    @PostMapping("/otp")
    public Response<ResultVo> sendOtp(@RequestBody OtpDto.SendOtpDto sendOtpDto) {
        String requestId = requestIdGenerator.getRequestId();
        ResultVo resultVo = authFacade.sendOtp(sendOtpDto);

        return Response.success(requestId, resultVo);
    }

    /**
     * 인증번호 확인 API
     */
    @PostMapping("/otp/check")
    public Response<ResultVo> otpCheck(@RequestBody OtpDto.CheckOtpDto checkOtpDto) {
        String requestId = requestIdGenerator.getRequestId();
        ResultVo resultVo = authFacade.checkOtp(checkOtpDto);

        return Response.success(requestId, resultVo);
    }

    /**
     * 사용자 로그인 API
     */
    @PostMapping("/login")
    public Response<UserLoginVo> login(@RequestBody UserLoginDto userLoginDto) {
        String requestId = requestIdGenerator.getRequestId();
        UserLoginVo loginVo = authFacade.login(userLoginDto);
        return Response.success(requestId, loginVo);
    }

    /**
     * 사용자 access token 재 발행 API
     */
    @PostMapping("/token/reissue")
    public Response<ReissueTokenVo> reissueToken(@RequestBody ReissueTokenDto reissueTokenDto) {
        String requestId = requestIdGenerator.getRequestId();
        return Response.success(requestId, authFacade.reissueToken(reissueTokenDto));
    }
}
