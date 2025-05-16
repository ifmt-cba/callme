package com.example.login_auth_api.controller;

import com.example.login_auth_api.dto.EmailLeituraCompletaDTO;
import com.example.login_auth_api.service.EmailReceiverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/email")
public class EmailReceiverController {

    private final EmailReceiverService emailReceiverService;

    public EmailReceiverController(EmailReceiverService emailReceiverService) {
        this.emailReceiverService = emailReceiverService;
    }

    @GetMapping("/receber")
    public List<EmailLeituraCompletaDTO> receberEmails() {
        return emailReceiverService.checkInbox();
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<Map<String, Object>>> listarResumoEmails() {
        List<EmailLeituraCompletaDTO> emails = emailReceiverService.checkInbox();

        List<Map<String, Object>> resposta = emails.stream().map(email -> {
            Map<String, Object> item = new HashMap<>();
            item.put("token", email.getToken());
            item.put("remetente", email.getRemetente());
            item.put("assunto", email.getAssunto());
            item.put("corpoSimples", email.getCorpoSimples());
            item.put("dataHora", email.getDataHora());
            return item;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }


}