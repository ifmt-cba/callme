package com.example.login_auth_api.controller;

import com.example.login_auth_api.dto.SendEmailDTO;
import com.example.login_auth_api.service.SendEmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class SendEmailController {
    private final SendEmailService emailService;

    public SendEmailController(SendEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public void sendEmail(@RequestBody SendEmailDTO email) {
        emailService.sendEmail(email);
        System.out.println(email);
    }
}
