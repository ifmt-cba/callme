package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.dto.EmailLeituraCompletaDTO;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.service.EmailReceiverService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @GetMapping("/completo")
    public List<EmailLeituraCompletaDTO> listarEmailsCompletos() {
        log.info("Requisição iniciada: listar e-mails completos | Hora: " + getTimestamp());

        List<EmailLeituraCompletaDTO> emails = emailReceiverService.checkInbox();

        log.info(String.format("E-mails completos listados | Quantidade: %d", emails.size()));
        return emails;
    }

    @GetMapping("/resumo")
    public List<EmailResumoDTO> listarResumos() {
        log.info("Requisição iniciada: listar resumos de e-mails | Hora: " + getTimestamp());

        List<EmailResumoDTO> resumos = emailReceiverService.listarResumosEmails();

        log.info(String.format("Resumos de e-mail listados | Quantidade: %d", resumos.size()));
        return resumos;
    }
}
