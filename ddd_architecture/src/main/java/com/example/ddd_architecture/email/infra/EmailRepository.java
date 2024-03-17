package com.example.ddd_architecture.email.infra;

import com.example.ddd_architecture.email.domain.Email;

public interface EmailRepository {
    Email save(Email createEmail);

    Email findById(Long emailId);
}
