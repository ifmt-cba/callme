package com.example.login_auth_api.controller;

import com.example.login_auth_api.dto.EmailLeituraCompletaDTO;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.service.EmailReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailReceiverController {

    @Autowired
    private EmailReceiverService emailReceiverService;

    @GetMapping("/completo")
    public List<EmailLeituraCompletaDTO> listarEmailsCompletos() {
        return emailReceiverService.checkInbox();
    }

    @GetMapping("/resumo")
    public List<EmailResumoDTO> listarResumos() {
        return emailReceiverService.listarResumosEmails();
    }
}
