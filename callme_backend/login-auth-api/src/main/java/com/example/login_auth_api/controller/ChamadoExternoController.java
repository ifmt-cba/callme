package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.dto.AtualizarStatusDTO;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import com.example.login_auth_api.service.ChamadoExternoService;
import com.example.login_auth_api.service.EmailReceiverService;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chamados")
public class ChamadoExternoController {

    private final EmailReceiverService emailReceiverService;
    private final ChamadoExternoService chamadoService;
    private final LogPort log;

    @Autowired
    private UserRepository userRepository; // Injete o repository de usuário

    public ChamadoExternoController(EmailReceiverService emailReceiverService,
                                    ChamadoExternoService chamadoService,
                                    LogPort log,
                                    UserRepository userRepository) {
        this.emailReceiverService = emailReceiverService;
        this.chamadoService = chamadoService;
        this.log = log;
        this.userRepository = userRepository;
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Endpoint para criar chamados a partir dos emails resumidos
    @GetMapping("/abrir")
    public List<ChamadoExterno> criarChamados() {
        log.info("Início: criar chamados a partir de emails | Hora: " + getTimestamp());

        List<EmailResumoDTO> resumos = emailReceiverService.listarResumosEmails();
        List<ChamadoExterno> chamados = chamadoService.criarChamadosAPartirDeEmails(resumos);

        log.info("Chamados criados com sucesso | Quantidade: " + chamados.size());
        return chamados;
    }

    @GetMapping("/listar")
    public List<ChamadoExterno> listarChamados() {
        log.info("Início: listar chamados | Hora: " + getTimestamp());

        List<ChamadoExterno> chamados = chamadoService.listarChamados();

        log.info("Chamados listados | Quantidade: " + chamados.size());
        return chamados;
    }

    @GetMapping("/buscar/{tokenEmail}")
    public ResponseEntity<ChamadoExterno> buscarChamadoPorToken(@PathVariable String tokenEmail) {
        log.info("Início: buscar chamado por token | Token: " + tokenEmail + " | Hora: " + getTimestamp());

        Optional<ChamadoExterno> chamado = chamadoService.buscarChamadoPorToken(tokenEmail);

        if (chamado.isPresent()) {
            log.info("Chamado encontrado | Token: " + tokenEmail);
            return ResponseEntity.ok(chamado.get());
        } else {
            log.warn("Chamado não encontrado | Token: " + tokenEmail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/editar/token/{tokenEmail}")
    public ResponseEntity<ChamadoExterno> editarChamadoPorToken(
            @PathVariable String tokenEmail,
            @RequestBody ChamadoExterno chamadoAtualizado) {

        log.info("Início: editar chamado por token | Token: " + tokenEmail + " | Hora: " + getTimestamp());

        Optional<ChamadoExterno> chamadoEditado = chamadoService.editarChamadoPorToken(tokenEmail, chamadoAtualizado);

        if (chamadoEditado.isPresent()) {
            log.info("Chamado editado com sucesso | Token: " + tokenEmail);
            return ResponseEntity.ok(chamadoEditado.get());
        } else {
            log.warn("Falha ao editar chamado | Token não encontrado: " + tokenEmail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    // NOVO ENDPOINT PARA O FRONTEND BUSCAR OS TÉCNICOS
    @GetMapping("/tecnicos")
    public ResponseEntity<List<User>> listarTecnicos() {
        log.info("Início: listar técnicos | Hora: " + getTimestamp());
        // CORREÇÃO: Usando o novo método para buscar pelo nome do perfil "RT" ou "ADMIN"
        List<User> tecnicos = userRepository.findByRoles_Name("RT");
        // Se seus técnicos forem administradores, use "ADMIN"
        // List<User> tecnicos = userRepository.findByRoles_Name("ADMIN");
        log.info("Técnicos listados | Quantidade: " + tecnicos.size());
        return ResponseEntity.ok(tecnicos);
    }
}