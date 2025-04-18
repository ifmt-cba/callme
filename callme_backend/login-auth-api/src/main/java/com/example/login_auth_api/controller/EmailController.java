package com.example.login_auth_api.controller;

import com.example.login_auth_api.dto.Email;
import com.example.login_auth_api.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public void sendEmail(@RequestBody Email email) {
        emailService.sendEmail(email);
        System.out.println(email);
    }
}
