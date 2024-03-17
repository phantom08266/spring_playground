package com.example.ddd_architecture.email.infra.entity;

import com.example.ddd_architecture.email.domain.Email;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email")
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String title;
    private String message;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EmailEntity from(Email createEmail) {
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.email = createEmail.getEmail();
        emailEntity.title = createEmail.getTitle();
        emailEntity.message = createEmail.getMessage();
        emailEntity.createdAt = LocalDateTime.now();
        emailEntity.updatedAt = LocalDateTime.now();
        return emailEntity;
    }

    public Email toDomain() {
        return Email.builder()
                .id(id)
                .email(email)
                .title(title)
                .message(message)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
