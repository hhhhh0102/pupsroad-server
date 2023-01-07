package com.onthe7.pupsroad.module.otp.application;

import com.onthe7.pupsroad.common.domain.service.RedisService;
import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.enums.MessageType;
import com.onthe7.pupsroad.common.exception.BusinessException;
import com.onthe7.pupsroad.common.exception.UserException;
import com.onthe7.pupsroad.common.util.CertUtil;
import com.onthe7.pupsroad.module.otp.domain.dto.OtpDto;
import com.onthe7.pupsroad.module.otp.domain.vo.OtpVo;
import com.onthe7.pupsroad.module.otp.domain.vo.SensMessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class OtpService {

    @Value("${api.redis-key-prefix}")
    private String REDIS_KEY_PREFIX;

    private final SensMessageService sensMessageService;
    private final RedisService redisService;

    /**
     * 인증번호 발송 및 캐시 저장
     * key: pupsroad:auth:otp:phone:{phone}
     * data: otp, created_at, expiredAt
     */
    @CachePut(value = "common-otp", key = "'auth:otp:phone:' + #inputDto.phone")
    public OtpVo.OtpInfo sendOtp(OtpDto.SendOtpDto inputDto) {

        // 같은 key로 캐시 생성시 이전 생성시각으로부터 1분이 지나야 캐시 생성
        OtpVo.OtpInfo redisData = getOtpCache(inputDto.getPhone());

        if (redisData != null) {
            log.debug("[OTP redisData]" + redisData);

            long nowAt = Timestamp.valueOf(LocalDateTime.now()).getTime();
            long updatePossibleAt = Timestamp.valueOf(LocalDateTime.parse(redisData.getCreatedAt()).plusMinutes(1)).getTime();

            if (updatePossibleAt >= nowAt) {
                throw new UserException.OtpHasAlReadyRequestedException();
            }
        }

        // 인증번호 생성 (4자리)
        String otpNumber = String.valueOf(CertUtil.genCertNumber(4));
        log.debug("[otpNumber]" + otpNumber);

        // 유효시간 3분
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(3);
        log.debug("[expiredAt]" + expiredAt);

        String subject = "인증번호 전송";
        String messageContents = "[인증번호: " + otpNumber + "] 본인확인 인증번호를 입력해 주세요.(3분간 유효합니다.)";
        SensMessageVo messageSendResponse = sensMessageService.sendSingleMessage(MessageType.SMS, subject, messageContents, inputDto.getPhone());

        log.info("#### Message Send - Start");

        // 메세지 전송실패
        if (!messageSendResponse.getStatusCode().equals("202")) {
            log.error("Message Send Failed");
            log.error(messageSendResponse.toString());

            throw new BusinessException(ErrorCode.COMMON_EXTERNAL_API_ERROR);
        }

        log.info("#### Message Send - End");

        return new OtpVo.OtpInfo(otpNumber, createdAt.toString(), expiredAt.toString());
    }

    /**
     * 인증번호 확인
     */
    @CacheEvict(value = "common-otp", key = "'auth:otp:phone:' + #inputDto.phone", condition = "#result != null")
    public boolean checkOtp(OtpDto.CheckOtpDto inputDto) {
        OtpVo.OtpInfo redisData = getOtpCache(inputDto.getPhone());

        if (redisData == null) { // redis에 otp 정보가 없으면 유저는 otp를 요청한 적이 없음
            throw new UserException.OtpNeverSentException();

        } else { // redisData != null
            log.debug("[OTP redisData]" + redisData);

            long nowAt = Timestamp.valueOf(LocalDateTime.now()).getTime();
            long expiredAt = Timestamp.valueOf(LocalDateTime.parse(redisData.getExpiredAt())).getTime();

            if (expiredAt <= nowAt) { // 유효기간 3분이 지났을때의 예외처리
                throw new UserException.ExpiredOtpException();

            } else if (inputDto.getOtp().equals(redisData.getOtp()) == false) { // 입력한 otp번호가 틀렸을 때
                throw new UserException.OtpCheckFailedException();

            } else {
                log.info("#### OTP matching success");
                return true;
            }
        }
    }

    /**
     * phone으로 Redis에 저장된 Otp 가져옴
     *
     * @param phone
     * @return
     */
    private OtpVo.OtpInfo getOtpCache(String phone) {
        // pupsroad:auth:otp:phone:{phone}
        String redisKey = REDIS_KEY_PREFIX + "auth:otp:phone:" + phone;
        return (OtpVo.OtpInfo) redisService.getValue(redisKey);
    }

}
