package com.onthe7.pupsroad.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NaverStorageClientConfig {

    private final AppProperties appProperties;

    @Bean
    public AmazonS3 naverStorageClient() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration(appProperties.getNcpStorageEndpoint(), appProperties.getNcpRegionName()))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(appProperties.getNcpAccessKey(), appProperties.getNcpSecretKey())))
                .build();
    }
}
