package com.onthe7.pupsroad.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    /* Spring */
    @Value("${spring.config.activate.on-profile}")
    private String activeProfile;

    /* Redis */
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.ssl}")
    private boolean useSSl;

    /* JWT */
    @Value("${app.auth.tokenIssuer}")
    private String tokenIssuer;

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${app.auth.tokenExpiry}")
    private int tokenExpiry;

    @Value("${app.auth.refreshTokenExpiry}")
    private int refreshTokenExpiry;

    /* Naver Cloud Platform */
    @Value("${app.ncp.object-storage.end-point}")
    private String ncpStorageEndpoint;

    @Value("${app.ncp.access-key}")
    private String ncpAccessKey;

    @Value("${app.ncp.secret-key}")
    private String ncpSecretKey;

    @Value("${app.ncp.region-name}")
    private String ncpRegionName;

    @Value("${app.ncp.object-storage.bucket-name}")
    private String resourceBucketName;

    @Value("${app.ncp.object-storage.resource.store-path}")
    private String resourceStorePath;
}
