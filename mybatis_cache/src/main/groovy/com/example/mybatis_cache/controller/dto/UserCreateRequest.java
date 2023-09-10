package com.example.mybatis_cache.controller.dto;


import lombok.Data;

@Data
public class UserCreateRequest {
    private String name;
    private String password;
    private String email;
}
