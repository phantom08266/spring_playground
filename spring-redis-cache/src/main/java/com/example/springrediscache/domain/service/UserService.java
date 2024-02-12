package com.example.springrediscache.domain.service;

import com.example.springrediscache.domain.entity.User;
import com.example.springrediscache.domain.repository.UserRespsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRespsitory userRespsitory;
    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;

    public User getUser(Long id) {
        return userRespsitory.findById(id).orElse(null);
    }

    public User getRedisTemplateUser(Long id) {
        String redisKey = "users:%d".formatted(id);
        User cachedUser = userRedisTemplate.opsForValue()
                .get(redisKey);

        if (Objects.nonNull(cachedUser)) return cachedUser;

        User user = userRespsitory.findById(id).orElseThrow(() -> new RuntimeException());
        userRedisTemplate.opsForValue()
                .set(redisKey, user, Duration.ofSeconds(30));
        return user;
    }

    public User getRedisTemplateObject(Long id) {
        String redisKey = "users:%d".formatted(id);
        User cachedUser = (User) objectRedisTemplate.opsForValue()
                .get(redisKey);

        if (Objects.nonNull(cachedUser)) return cachedUser;

        User user = userRespsitory.findById(id).orElseThrow(() -> new RuntimeException());
        objectRedisTemplate.opsForValue()
                .set(redisKey, user, Duration.ofSeconds(30));
        return user;
    }
}
