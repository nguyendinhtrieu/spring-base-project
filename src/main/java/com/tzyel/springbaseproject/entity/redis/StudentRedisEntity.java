package com.tzyel.springbaseproject.entity.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Student")
@Getter
@Setter
public class StudentRedisEntity implements Serializable {
    private String id;
    private String name;
}
