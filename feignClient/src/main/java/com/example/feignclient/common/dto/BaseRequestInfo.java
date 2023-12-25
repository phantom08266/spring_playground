package com.example.feignclient.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class BaseRequestInfo {
    private String name;
    private int age;
}
