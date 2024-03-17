package com.example.ddd_architecture.email.controller.v1;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateEmailDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String email;
        private String title;
        private String message;
    }
}
