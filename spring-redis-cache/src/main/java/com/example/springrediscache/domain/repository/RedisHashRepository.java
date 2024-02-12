package com.example.springrediscache.domain.repository;

import com.example.springrediscache.domain.entity.RedisHashUser;
import org.springframework.data.repository.CrudRepository;

public interface RedisHashRepository extends CrudRepository<RedisHashUser, Long> {
}
