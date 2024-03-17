package com.example.ddd_architecture.email.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

import static org.springframework.util.Assert.notNull;

@Getter
@Builder
public class Email {
    private long id;
    private String email;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Email of(String email, String title, String message) {
        notNull(email, "email must not be null");
        notNull(title, "title must not be null");
        notNull(message, "message must not be null");

        return Email.builder()
                .email(email)
                .title(title)
                .message(message)
                .build();
    }
}
