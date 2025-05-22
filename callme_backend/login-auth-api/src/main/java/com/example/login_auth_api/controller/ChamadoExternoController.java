package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.dto.AtualizarStatusDTO;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.service.ChamadoExternoService;
import com.example.login_auth_api.service.EmailReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        List<EmailResumoDTO> resumos = emailReceiverService.listarResumosEmails();
        return chamadoService.criarChamadosAPartirDeEmails(resumos);
    }

    @GetMapping("/listar")
    public List<ChamadoExterno> listarChamados() {
        return chamadoService.listarChamados();
    }

    @GetMapping("/buscar/{tokenEmail}")
    public ResponseEntity<ChamadoExterno> buscarChamadoPorToken(@PathVariable String tokenEmail) {
        Optional<ChamadoExterno> chamado = chamadoService.buscarChamadoPorToken(tokenEmail);
        return chamado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }




}