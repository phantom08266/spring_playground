package com.example.mybatis_cache.service;

import com.example.mybatis_cache.entity.User;
import com.example.mybatis_cache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public int createUser(String name, String email, String password) {
        User user = User.createUser(name, email, password);
        log.info("Repository 호출 전");
        userRepository.save(user);
        log.info("Repository 호출 후");
        return user.getId();
    }

    @Transactional
    public User getUser(int id) {
        log.info("Repository 호출 전");
        User user = userRepository.findByIdNoFlush(id);
        log.info("Repository 호출 후");
        return user;
    }

    @Transactional
    public User getUserNCallNoFlush(int id) {
        log.info("Repository 호출 전");
        User user = userRepository.findByIdNoFlush(id);

        log.info("두번째 호출");
        User user2 = userRepository.findByIdNoFlush(id);

        log.info("Repository 호출 후");
        return user;
    }

    @Transactional
    public User getUserNCallFlush(int id) {
        log.info("Repository 호출 전");
        User user = userRepository.findByIdFlush(id);

        log.info("두번째 호출");
        User user2 = userRepository.findByIdFlush(id);

        log.info("Repository 호출 후");
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findUsersForLoop() {
        final int loopSize = 3;
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= loopSize; i++) {
            log.info("Repository 호출 전");
            User user = userRepository.findByIdNoFlush(i);
            log.info("user 주소 : " + user);
            users.add(user);
            log.info("Repository 호출 후");
        }
        return users;
    }
}
