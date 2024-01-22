package com.tzyel.springbaseproject.repository.redis;

import com.tzyel.springbaseproject.entity.redis.StudentRedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRedisRepository extends CrudRepository<StudentRedisEntity, String> {
}
