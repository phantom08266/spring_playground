package com.example.ddd_architecture.email.infra;

import com.example.ddd_architecture.email.infra.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJpaRepository extends JpaRepository<EmailEntity, Long> {

}
