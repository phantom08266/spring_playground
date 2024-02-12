package com.example.springrediscache.domain.controller;

import com.example.springrediscache.domain.entity.RedisHashUser;
import com.example.springrediscache.domain.entity.User;
import com.example.springrediscache.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/redis/{id}")
    public User getRedisTemplateUser(@PathVariable Long id) {
        return userService.getRedisTemplateUser(id);
    }

    @GetMapping("/users/redis/object/{id}")
    public User getRedisTemplateObject(@PathVariable Long id) {
        return userService.getRedisTemplateObject(id);
    }

    @GetMapping("/users/redis/hash/{id}")
    public RedisHashUser getRedisHashUser(@PathVariable Long id) {
        return userService.getRedisHashUser(id);
    }

    @GetMapping("/users/redis/cacheable/{id}")
    public User getCacheableUser(@PathVariable Long id) {
        return userService.redisCacheableUser(id);
    }
}
