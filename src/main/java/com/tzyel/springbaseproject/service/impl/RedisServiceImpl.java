package com.tzyel.springbaseproject.service.impl;

import com.tzyel.springbaseproject.service.RedisService;
import com.tzyel.springbaseproject.utils.ObjectMapperUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    private static final Jackson2JsonRedisSerializer<Object> OBJECT_JACKSON_2_JSON_REDIS_SERIALIZER = new Jackson2JsonRedisSerializer<>(Object.class);
    private static final StringRedisSerializer STRING_REDIS_SERIALIZER = new StringRedisSerializer();

    private final RedisTemplate<String, Object> template;

    public RedisServiceImpl(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    @Override
    public String getValue(String key) {
        Object value;
        synchronized (this) {
            template.setHashValueSerializer(STRING_REDIS_SERIALIZER);
            template.setValueSerializer(STRING_REDIS_SERIALIZER);
            value = template.opsForValue().get(key);
        }
        return value == null ? null : value.toString();
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        new Jackson2JsonRedisSerializer<>(Object.class);
        Object value;
        synchronized (this) {
            template.setHashValueSerializer(OBJECT_JACKSON_2_JSON_REDIS_SERIALIZER);
            template.setValueSerializer(OBJECT_JACKSON_2_JSON_REDIS_SERIALIZER);
            value = template.opsForValue().get(key);
        }
        return ObjectMapperUtil.map(value, clazz);
    }

    @Override
    public void setValue(String key, Object value, long timeToLive, boolean marshal) {
        if (marshal) {
            template.setHashValueSerializer(OBJECT_JACKSON_2_JSON_REDIS_SERIALIZER);
            template.setValueSerializer(OBJECT_JACKSON_2_JSON_REDIS_SERIALIZER);
        } else {
            template.setHashValueSerializer(new StringRedisSerializer());
            template.setValueSerializer(new StringRedisSerializer());
        }

        template.opsForValue().set(key, value);
        template.expire(key, timeToLive, TimeUnit.SECONDS);
    }
}
