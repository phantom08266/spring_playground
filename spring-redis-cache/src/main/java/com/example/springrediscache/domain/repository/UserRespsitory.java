package com.example.springrediscache.domain.repository;

import com.example.springrediscache.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespsitory extends JpaRepository<User, Long> {
}
