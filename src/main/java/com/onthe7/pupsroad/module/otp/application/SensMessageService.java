package com.onthe7.pupsroad.module.otp.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.enums.MessageType;
import com.onthe7.pupsroad.common.exception.BusinessException;
import com.onthe7.pupsroad.module.otp.domain.dto.SensMessageDto;
import com.onthe7.pupsroad.module.otp.domain.vo.SensMessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SensMessageService {

    @Value("${naver.sens.endpoint}")
    private String ENDPOINT;

    @Value("${naver.sens.sms-url}")
    private String SMS_URL;

    @Value("${naver.sens.access-key}")
    private String ACCESS_KEY;

    @Value("${naver.sens.secret-key}")
    private String SECRET_KEY;

    @Value("${naver.sens.sender-no}")
    private String SENDER_NO;

    public SensMessageVo sendSingleMessage(MessageType messageType, String subject, String content, String to) {
        // 발송객체 Set
        SensMessageDto sensMessageDto = new SensMessageDto();
        sensMessageDto.setType(messageType.getValue()); // 메세지유형 (SMS/LMS/MMS)
        sensMessageDto.setFrom(SENDER_NO);  // 발신자 전화번호

        sensMessageDto.setSubject(subject); // 제목
        sensMessageDto.setContent(content); // 내용

        SensMessageDto.Message message = new SensMessageDto.Message(to);
        sensMessageDto.setMessages(List.of(message)); // 수신자목록

        log.debug("#######[sensMessageDto]" + sensMessageDto.toString());
        return sendMessage(sensMessageDto);
    }

    public SensMessageVo sendMessage(SensMessageDto sensMessageDto) {
        try {
            OkHttpClient client = new OkHttpClient();
            ObjectMapper objectMapper = new ObjectMapper();

            String sendUrl = ENDPOINT + SMS_URL;

            HttpUrl httpUrl = HttpUrl.parse(sendUrl)
                    .newBuilder()
                    .build();

            String unixTimestamp = Long.toString(System.currentTimeMillis());
            String signature = makeSignature(SMS_URL, unixTimestamp, ACCESS_KEY, SECRET_KEY);

            String requestBodyContent = objectMapper.writeValueAsString(sensMessageDto);
            log.info(requestBodyContent);

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(httpUrl)
                    .addHeader("x-ncp-apigw-timestamp", unixTimestamp)
                    .addHeader("x-ncp-iam-access-key", ACCESS_KEY)
                    .addHeader("x-ncp-apigw-signature-v2", signature)
                    .post(RequestBody.create(requestBodyContent, okhttp3.MediaType.parse("application/json")))
                    .build();

            okhttp3.Response httpResponse = client.newCall(httpRequest).execute();

            if (httpResponse.isSuccessful()) {
                SensMessageVo response = objectMapper.readValue(httpResponse.body().string(), new TypeReference<>() {
                });
                return response;

            } else {
                throw new BusinessException(ErrorCode.COMMON_SYSTEM_ERROR);
            }

        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.COMMON_SYSTEM_ERROR);
        }
    }

    private String makeSignature(String url, String timestamp, String accessKey, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";

        String message = new StringBuilder()
                .append(method)         // method
                .append(space)          // one space
                .append(url)            // url (include query string)
                .append(newLine)        // new line
                .append(timestamp)      // current timestamp (epoch)
                .append(newLine)
                .append(accessKey)      // access key id (from portal or Sub Account)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(rawHmac);
    }
}
