package com.example.feignclient.service;

import com.example.feignclient.common.dto.BaseResponseInfo;
import com.example.feignclient.feign.client.DemoFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemoService {
    private final DemoFeignClient demoFeignClient;

    public void callGet() {
        ResponseEntity<BaseResponseInfo> response = demoFeignClient.callGet("customHeader", "name", 10);
        log.info("response: {}", response);
    }
}
