package com.example.vuejsspringboot.controller;

import com.example.vuejsspringboot.controller.dto.CreatePost;
import com.example.vuejsspringboot.service.PostService;
import com.example.vuejsspringboot.service.vo.PostVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public PostVo create(@RequestBody @Valid CreatePost request) {
        log.info("[PostController] request: {}", request);
        PostVo post = postService.create(request.getTitle(), request.getContent());
        log.info("[PostController] save post: {}", post);
        return post;
    }
}
