package com.tzyel.springbaseproject;

import com.github.fppt.jedismock.RedisServer;
import com.tzyel.springbaseproject.config.properties.RedisProperties;
import com.tzyel.springbaseproject.entity.redis.StudentRedisEntity;
import com.tzyel.springbaseproject.repository.redis.StudentRedisRepository;
import com.tzyel.springbaseproject.service.RedisService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = UnitTestConfiguration.class)
public class RedisTest {
    private static RedisServer redisServer;

    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private RedisService redisService;
    @Autowired
    private StudentRedisRepository studentRedisRepository;

    @PostConstruct
    public void postConstruct() throws IOException {
        if (redisServer == null) {
            redisServer = RedisServer.newRedisServer(redisProperties.getPort());
            redisServer.start();
        }
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        if (redisServer != null) {
            redisServer.stop();
            redisServer = null;
        }
    }

    @Test
    public void testRedis() {
        StudentRedisEntity studentRedisEntity = new StudentRedisEntity();
        studentRedisEntity.setId(new Date().toString());
        studentRedisEntity.setName("Name 1");
        studentRedisRepository.save(studentRedisEntity);

        studentRedisRepository.findAll();

        redisService.setValue("KEY", studentRedisEntity, 100000000, true);
        redisService.getValue("KEY");
    }
}
