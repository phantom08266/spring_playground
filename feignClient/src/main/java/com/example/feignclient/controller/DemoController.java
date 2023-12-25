package com.example.feignclient.controller;


import com.example.feignclient.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final DemoService demoService;

    @GetMapping("/call")
    public void callGet() {
        demoService.callGet();
    }

    @GetMapping("/create")
    public void create() {
        demoService.create();
    }

    @GetMapping("/error")
    public void error() {
        demoService.error();
    }

}
