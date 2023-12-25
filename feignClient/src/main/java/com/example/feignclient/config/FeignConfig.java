package com.example.feignclient.config;

import com.example.feignclient.feign.logger.FeignCustomLogger;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Logger logger() {
        return new FeignCustomLogger();
    }
}
