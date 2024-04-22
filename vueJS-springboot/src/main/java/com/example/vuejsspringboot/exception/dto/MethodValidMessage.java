package com.example.vuejsspringboot.exception.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MethodValidMessage {
    private final int code;
    private final String message;
    private Map<String, String> detail = new HashMap<>();

    public void addDetail(String field, String message) {
        detail.put(field, message);
    }

}
