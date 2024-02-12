package com.example.springrediscache.domain.service;

import com.example.springrediscache.domain.entity.User;
import com.example.springrediscache.domain.repository.UserRespsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRespsitory userRespsitory;

    public User getUser(Long id) {
        return userRespsitory.findById(id).orElse(null);
    }
}
