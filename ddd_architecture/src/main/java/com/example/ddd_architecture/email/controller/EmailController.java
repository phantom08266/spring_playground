package com.example.ddd_architecture.email.controller;

import com.example.ddd_architecture.email.controller.v1.CreateEmailDto;
import com.example.ddd_architecture.email.controller.v1.FindEmailDto;
import com.example.ddd_architecture.email.domain.Email;
import com.example.ddd_architecture.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/v1/email")
    public void createEmail(@RequestBody CreateEmailDto.Request request) {
        emailService.sendEmail(request.getEmail(), request.getTitle(), request.getMessage());
    }

    @GetMapping("/v1/emails/{emailId}")
    public FindEmailDto.Response findEmail(@PathVariable Long emailId) {
        Email email = emailService.findEmail(emailId);
        return new FindEmailDto.Response(email);
    }
}
