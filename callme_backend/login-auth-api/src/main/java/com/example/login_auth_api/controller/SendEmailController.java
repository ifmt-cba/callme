package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.dto.SendEmailDTO;
import com.example.login_auth_api.service.SendEmailService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("email")
public class SendEmailController {

    private final SendEmailService emailService;
    private final LogPort log;

    public SendEmailController(SendEmailService emailService, LogPort log) {
        this.emailService = emailService;
        this.log = log;
    }

    @PostMapping
    public void sendEmail(@RequestBody SendEmailDTO email) {
        log.info(String.format("Enviando e-mail | Para: %s | Assunto: %s | Hora: %s", email.to(), email.subject()));

        emailService.sendEmail(email);

        log.info(String.format("E-mail enviado com sucesso | Para: %s | Assunto: %s", email.to(), email.subject()));
    }
}
