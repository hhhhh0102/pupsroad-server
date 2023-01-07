package com.onthe7.pupsroad.common.external.ncp;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.onthe7.pupsroad.common.config.AppProperties;
import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
@RequiredArgsConstructor
public class NaverObjectStorageService {

    private final AppProperties appProperties;
    private final AmazonS3 naverStorageClient;

    public String uploadObject(MultipartFile file) {

        String bucketName = appProperties.getResourceBucketName() + "/" + appProperties.getResourceStorePath();
        String key = buildPutObjectRequestKey(file);
        ObjectMetadata metadata = new ObjectMetadata();
        try {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            naverStorageClient.putObject(new PutObjectRequest(bucketName, key, byteArrayInputStream, metadata));

            return appProperties.getNcpStorageEndpoint() + "/" + bucketName + "/" + key;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.COMMON_SYSTEM_ERROR);
        }
    }

    private String buildPutObjectRequestKey(MultipartFile file) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = formatter.format(new Date());
        String fileExtension = getExtension(file.getOriginalFilename());

        return dateTime + "-" + randomUUID() + "." + fileExtension;
    }
}
