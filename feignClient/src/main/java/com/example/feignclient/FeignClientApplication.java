package com.example.feignclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients // FeignClient를 사용하기 위해 필요한 어노테이션
@SpringBootApplication
public class FeignClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignClientApplication.class, args);
	}

}
