package com.example.mybatis_cache.controller;

import com.example.mybatis_cache.controller.dto.UserCreateRequest;
import com.example.mybatis_cache.entity.User;
import com.example.mybatis_cache.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/create")
    public int createUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request.getName(), request.getEmail(), request.getPassword());
    }

    @GetMapping("/one/{id}")
    public int findByUser1Call(@PathVariable int id) {
        return userService.getUser(id).getId();
    }

    @GetMapping("/multi/{id}")
    public int findByUserNCall(@PathVariable int id) {
        return userService.getUserNCallNoFlush(id).getId();
    }

    @GetMapping("/multi/flush/{id}")
    public int findByUserNCallFlush(@PathVariable int id) {
        return userService.getUserNCallFlush(id).getId();
    }

    @GetMapping("/no-cache/for-loop")
    public List<User> findAllNoCache() {
        return userService.findUsersForLoop();
    }
}