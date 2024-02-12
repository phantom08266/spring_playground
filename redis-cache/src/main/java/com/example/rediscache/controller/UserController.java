package com.example.rediscache.controller;

import com.example.rediscache.entity.User;
import com.example.rediscache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JedisPool jedisPool;
    @GetMapping("/users/{id}/email")
    public String getUserEmail(@PathVariable Long id) {
        try (Jedis jedis = jedisPool.getResource()){
            String userEmailRedisKey = "users:%d:email".formatted(id);
            String userEmail = jedis.get(userEmailRedisKey);
            if (Objects.nonNull(userEmail)) {
                return userEmail;
            }
            userEmail = userRepository.findById(id)
                    .orElse(User.builder().build())
                    .getEmail();

            jedis.set(userEmailRedisKey, userEmail);
            // TTL 설정
            jedis.setex(userEmailRedisKey, 30, userEmail);
            return userEmail;
        }
    }
}
