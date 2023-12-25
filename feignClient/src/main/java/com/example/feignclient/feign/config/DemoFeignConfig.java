package com.example.feignclient.feign.config;


import com.example.feignclient.feign.interceptor.DemoFeignInterceptor;
import org.springframework.context.annotation.Bean;

public class DemoFeignConfig {

    @Bean
    public DemoFeignInterceptor demoFeignInterceptor() {
        return DemoFeignInterceptor.ofDefault();
    }
}
