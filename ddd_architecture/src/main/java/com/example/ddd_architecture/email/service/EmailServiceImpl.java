package com.example.ddd_architecture.email.service;

import com.example.ddd_architecture.email.domain.Email;
import com.example.ddd_architecture.email.infra.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;

    @Override
    @Transactional
    public Email sendEmail(String email, String title, String message) {
        Email createEmail = Email.of(email, title, message);
        return emailRepository.save(createEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public Email findEmail(Long emailId) {
        return emailRepository.findById(emailId);
    }
}
