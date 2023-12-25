package com.example.feignclient.feign.config;


import com.example.feignclient.feign.decoder.DemoFeignErrorDecoder;
import com.example.feignclient.feign.interceptor.DemoFeignInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoFeignConfig {

    @Bean
    public DemoFeignInterceptor demoFeignInterceptor() {
        return DemoFeignInterceptor.ofDefault();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new DemoFeignErrorDecoder();
    }
}
