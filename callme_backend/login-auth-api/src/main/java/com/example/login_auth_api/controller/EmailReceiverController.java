package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.dto.EmailLeituraCompletaDTO;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.service.EmailReceiverService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailReceiverController {

    private final EmailReceiverService emailReceiverService;
    private final LogPort log;

    public EmailReceiverController(EmailReceiverService emailReceiverService, LogPort log) {
        this.emailReceiverService = emailReceiverService;
        this.log = log;
    }

    @GetMapping("/completo")
    public List<EmailLeituraCompletaDTO> listarEmailsCompletos() {
        log.info("Listando e-mails completos");

        List<EmailLeituraCompletaDTO> emails = emailReceiverService.checkInbox();

        log.info(String.format("E-mails completos listados | Quantidade: %d", emails.size()));
        return emails;
    }

    @GetMapping("/resumo")
    public List<EmailResumoDTO> listarResumos() {
        log.info("Listando resumos de e-mails");

        List<EmailResumoDTO> resumos = emailReceiverService.listarResumosEmails();

        log.info(String.format("Resumos de e-mail listados | Quantidade: %d", resumos.size()));
        return resumos;
    }
}
