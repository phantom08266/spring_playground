package com.example.vuejsspringboot.controller;

import com.example.vuejsspringboot.controller.dto.CreatePost;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/api/posts")
    public void create(@RequestBody @Valid CreatePost request) {
        log.info("[PostController] request: {}", request);
    }
}
