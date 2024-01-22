package com.tzyel.springbaseproject.service;

public interface RedisService {

    String getValue(String key);

    <T> T getValue(String key, Class<T> clazz);

    void setValue(String key, Object value, long timeToLive, boolean marshal);
}
