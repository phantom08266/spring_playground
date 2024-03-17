package com.example.ddd_architecture.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health_check.html")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity
            .ok()
            .build();
    }
}