package com.example.ddd_architecture.post.controller;

import com.example.ddd_architecture.post.controller.dto.PostCreateDto;
import com.example.ddd_architecture.post.controller.dto.PostResponse;
import com.example.ddd_architecture.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostCreateController {

    private final PostService postService;
    private final PostController postController;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreateDto postCreateDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postController.toResponse(postService.createPost(postCreateDto)));
    }
}