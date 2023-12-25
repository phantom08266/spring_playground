package com.example.feignclient.controller;

import com.example.feignclient.common.dto.BaseRequestInfo;
import com.example.feignclient.common.dto.BaseResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ServerController {

    @GetMapping("/server/get")
    public BaseResponseInfo get(
            @RequestHeader("CustomHeaderName") String customHeader,
            @RequestParam("name") String name,
            @RequestParam("age") int age
//            @ModelAttribute BaseRequestInfo request
    ) {

        log.info("customHeader: {}", customHeader);
//        log.info("request: {}", request);
        log.info("name: {}", name);
        log.info("age: {}", age);
        return BaseResponseInfo.builder()
                .header(customHeader)
                .name(name)
                .age(age)
                .build();
    }
}
