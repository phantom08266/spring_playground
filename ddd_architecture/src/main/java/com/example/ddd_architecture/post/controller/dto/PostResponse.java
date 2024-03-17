package com.example.ddd_architecture.post.controller.dto;

import com.example.ddd_architecture.user.controller.dto.UserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponse writer;
}
