package com.example.ddd_architecture.email.controller.v1;

import com.example.ddd_architecture.email.domain.Email;

import java.time.LocalDateTime;

public class FindEmailDto {

    public record Response(
            Long id,
            String email,
            String title,
            String message,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        public Response(Email email) {
            this(email.getId(), email.getEmail(), email.getTitle(), email.getMessage(), email.getCreatedAt(), email.getUpdatedAt());
        }
    }
}
