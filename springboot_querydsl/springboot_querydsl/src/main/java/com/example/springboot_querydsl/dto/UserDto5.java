package com.example.springboot_querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.ToString;

@ToString
public class UserDto5 {
    private String name;
    private int age;

    @QueryProjection
    public UserDto5(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
