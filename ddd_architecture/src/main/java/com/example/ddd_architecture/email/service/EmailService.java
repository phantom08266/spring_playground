package com.example.ddd_architecture.email.service;

import com.example.ddd_architecture.email.domain.Email;

public interface EmailService {
    Email sendEmail(String email, String title, String message);

    Email findEmail(Long emailId);
}
