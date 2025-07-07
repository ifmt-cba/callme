package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.dto.SendEmailDTO;
import com.example.login_auth_api.service.SendEmailService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("email")
public class SendEmailController {

    private final SendEmailService emailService;
    private final LogPort log;

    public SendEmailController(SendEmailService emailService, LogPort log) {
        this.emailService = emailService;
        this.log = log;
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @PostMapping
    public void sendEmail(@RequestBody SendEmailDTO email) {
        log.info(String.format(
                "Início do envio de e-mail | Para: %s | Assunto: %s | Hora: %s",
                email.to(), email.subject(), now()
        ));

        emailService.sendEmail(email);

        log.info(String.format(
                "E-mail enviado com sucesso | Para: %s | Assunto: %s", 
                email.to(), email.subject()
        ));
    }
}
