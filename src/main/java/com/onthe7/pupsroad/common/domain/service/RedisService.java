package com.onthe7.pupsroad.common.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate redisTemplate;

    private ValueOperations<String, Object> values;

    @PostConstruct
    public void init() {
        this.values = this.redisTemplate.opsForValue();
    }

    /**
     * 캐시 저장 - TTL 지정x
     *
     * @param key
     * @param value
     */
    public void setValue(String key, Object value) {
        values.set(key, value);
    }

    /**
     * 캐시 저장 - TTL 지정o
     *
     * @param key
     * @param value
     * @param duration
     */
    public void setValue(String key, Object value, Duration duration) {
        values.set(key, value, duration);
    }

    /**
     * key로 value 찾기
     *
     * @param key
     * @return
     */
    public Object getValue(String key) {
        if (redisTemplate.hasKey(key)) {
            return values.get(key);
        }
        return null;
    }

    /**
     * 캐시 삭제
     *
     * @param key
     */
    public void deleteValue(List<String> key) {
        redisTemplate.delete(key);
    }

    /**
     * keyPattern 으로 KeyList 검색
     *
     * @param keyPattern
     * @return
     */
    public List<String> getKeyList(String keyPattern) {
        List<String> keyList = new ArrayList<>();
        redisTemplate.keys(keyPattern).forEach(key -> keyList.add((String) key));

        return keyList;
    }

}
