package com.example.vuejsspringboot.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePost {
    @NotBlank(message = "타이틀은 필수값 입니다") private final String title;
    @NotBlank(message = "컨텐츠는 필수값 입니다") private final String content;
}
