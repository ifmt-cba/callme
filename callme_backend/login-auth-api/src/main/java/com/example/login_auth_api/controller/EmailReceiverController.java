package com.example.login_auth_api.controller;

import com.example.login_auth_api.dto.EmailDTO;
import com.example.login_auth_api.service.EmailReceiverService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailReceiverController {

    private final EmailReceiverService emailReceiverService;

    public EmailReceiverController(EmailReceiverService emailReceiverService) {
        this.emailReceiverService = emailReceiverService;
    }

    @GetMapping("/receber")
    public List<EmailDTO> receberEmails() {
        return emailReceiverService.checkInbox();
    }
}