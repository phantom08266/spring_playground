package com.example.ddd_architecture.email.infra;

import com.example.ddd_architecture.email.domain.Email;
import com.example.ddd_architecture.email.infra.entity.EmailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailRepositoryImpl implements EmailRepository {
    private final EmailJpaRepository emailJpaRepository;

    @Override
    public Email save(Email createEmail) {
        return emailJpaRepository.save(EmailEntity.from(createEmail)).toDomain();
    }

    @Override
    public Email findById(Long emailId) {
        return emailJpaRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found."))
                .toDomain();
    }
}
