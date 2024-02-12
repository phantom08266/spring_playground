package com.example.rediscache;

import com.example.rediscache.entity.User;
import com.example.rediscache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class RedisCacheApplication implements ApplicationRunner {

    private final UserRepository userRepository;
    public static void main(String[] args) {
        SpringApplication.run(RedisCacheApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userRepository.save(User.builder().name("John Doe").email("test@ftest.com").build());
        userRepository.save(User.builder().name("tomy").email("tomy@ttt.com").build());
        userRepository.save(User.builder().name("test1").email("test1@test1.com").build());
        userRepository.save(User.builder().name("test3").email("test1@test3.com").build());
    }
}
