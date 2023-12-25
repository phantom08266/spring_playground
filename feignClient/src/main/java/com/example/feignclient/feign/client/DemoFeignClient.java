package com.example.feignclient.feign.client;

import com.example.feignclient.common.dto.BaseRequestInfo;
import com.example.feignclient.common.dto.BaseResponseInfo;
import com.example.feignclient.feign.config.DemoFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "demo-client", // yml에 설정한 service name
        url = "${feign.url.prefix}",
        configuration = DemoFeignConfig.class
)
public interface DemoFeignClient {

    @GetMapping("/get")
    ResponseEntity<BaseResponseInfo> callGet(
            @RequestHeader("CustomHeaderName") String customHeader,
            @RequestParam("name") String name,
            @RequestParam("age") int age
    );

    @PostMapping("/post")
     ResponseEntity<BaseResponseInfo> create(
            @RequestHeader("CustomHeaderName") String customHeader,
            @RequestBody BaseRequestInfo request
    );
}
