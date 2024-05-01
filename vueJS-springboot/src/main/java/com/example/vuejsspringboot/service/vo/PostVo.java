package com.example.vuejsspringboot.service.vo;

import com.example.vuejsspringboot.entity.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostVo {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostVo(Post savedPost) {
        this.id = savedPost.getId();
        this.title = savedPost.getTitle();
        this.content = savedPost.getContent();
        this.createdAt = savedPost.getCreatedAt();
        this.updatedAt = savedPost.getUpdatedAt();
    }
}
