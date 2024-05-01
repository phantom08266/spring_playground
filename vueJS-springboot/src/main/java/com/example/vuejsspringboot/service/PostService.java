package com.example.vuejsspringboot.service;

import com.example.vuejsspringboot.entity.Post;
import com.example.vuejsspringboot.repository.PostRepository;
import com.example.vuejsspringboot.service.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostVo create(String title, String content) {
        Post savedPost = postRepository.save(
                Post.builder()
                        .title(title)
                        .content(content)
                        .build());
        return new PostVo(savedPost);
    }
}
