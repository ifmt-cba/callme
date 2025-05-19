package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.service.ChamadoExternoService;
import com.example.login_auth_api.service.EmailReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoExternoController {

    @Autowired
    private EmailReceiverService emailReceiverService;

    @Autowired
    private ChamadoExternoService chamadoService;

    // Endpoint para criar chamados a partir dos emails resumidos
    @GetMapping(    "/abrir")
    public List<ChamadoExterno> criarChamados() {
        // Pega os resumos de email
        List<EmailResumoDTO> resumos = emailReceiverService.listarResumosEmails();

        // Cria chamados a partir desses resumos
        return chamadoService.criarChamadosAPartirDeEmails(resumos);
    }

    // Opcional: listar todos chamados
    @GetMapping
    public List<ChamadoExterno> listarChamados() {
        return chamadoService.listarChamados();
    }
}
