package com.example.ddd_architecture.post.service;

import com.example.ddd_architecture.common.domain.exception.ResourceNotFoundException;
import com.example.ddd_architecture.post.controller.dto.PostCreateDto;
import com.example.ddd_architecture.post.controller.dto.PostUpdateDto;
import com.example.ddd_architecture.post.infra.PostEntity;
import com.example.ddd_architecture.post.infra.PostRepository;
import com.example.ddd_architecture.user.infra.UserEntity;
import com.example.ddd_architecture.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostEntity getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public PostEntity createPost(PostCreateDto postCreateDto) {
        UserEntity userEntity = userService.getByIdOrElseThrow(postCreateDto.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreateDto.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }

    public PostEntity updatePost(long id, PostUpdateDto postUpdateDto) {
        PostEntity postEntity = getPostById(id);
        postEntity.setContent(postUpdateDto.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }
}