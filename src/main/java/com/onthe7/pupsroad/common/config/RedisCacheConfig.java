package com.onthe7.pupsroad.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
@EnableCaching(proxyTargetClass = true)
public class RedisCacheConfig extends CachingConfigurerSupport {

    private final AppProperties appProperties;

    @Bean
    public RedisConnectionFactory cacheRedisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(
                appProperties.getRedisHost(), appProperties.getRedisPort());

        standaloneConfiguration.setPassword(appProperties.getRedisPassword());
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();

        if (appProperties.isUseSSl()) {
            builder.useSsl();
        }
        return new LettuceConnectionFactory(standaloneConfiguration, builder.build());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cacheRedisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cacheRedisConnectionFactory());

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .prefixKeysWith("pupsroad:");

        Map<String, RedisCacheConfiguration> cacheConfigurations = setEntryTTL(configuration);

        return builder.cacheDefaults(configuration).withInitialCacheConfigurations(cacheConfigurations).build();
    }

    /**
     * default CacheManager 메소드별 캐시 유효기간 설정
     *
     * @param configuration
     * @return
     */
    private Map<String, RedisCacheConfiguration> setEntryTTL(RedisCacheConfiguration configuration) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // SMS 인증번호 - 10분
        cacheConfigurations.put("common-otp", configuration.entryTtl(Duration.ofMinutes(10L)));

        return cacheConfigurations;
    }
}
