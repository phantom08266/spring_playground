package com.example.springrediscache;

import com.example.springrediscache.domain.entity.User;
import com.example.springrediscache.domain.repository.UserRespsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class SpringRedisCacheApplication implements ApplicationRunner {

    private final UserRespsitory userRespsitory;
    public static void main(String[] args) {
        SpringApplication.run(SpringRedisCacheApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userRespsitory.save(User.builder().name("noa").email("noa@test.com").build());
        userRespsitory.save(User.builder().name("test").email("test@test.com").build());
        userRespsitory.save(User.builder().name("kait").email("kait@test.com").build());
    }
}
