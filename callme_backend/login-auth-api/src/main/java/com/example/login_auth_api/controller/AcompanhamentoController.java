package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.dto.AcompanhamentoDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestController
@RequestMapping("/acompanhamentos")
public class AcompanhamentoController {

    private final ChamadoExternoRepository chamadoExternoRepository;
    private final LogPort log;

    public AcompanhamentoController(ChamadoExternoRepository chamadoExternoRepository, LogPort log) {
        this.chamadoExternoRepository = chamadoExternoRepository;
        this.log = log;
    }

    @GetMapping("/{token}")
    public ResponseEntity<AcompanhamentoDTO> getAcompanhamentoPorToken(@PathVariable String token) {
        log.info(String.format("Buscando acompanhamento público para o token: %s", token));

        return chamadoExternoRepository.findByTokenEmail(token)
                .map(chamado -> {
                    // ✅ CORREÇÃO: Converte a String de data para LocalDateTime e depois para Instant
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy 'às' HH:mm", new Locale("pt", "BR"));
                    LocalDateTime localDateTime = LocalDateTime.parse(chamado.getDataHora(), formatter);

                    AcompanhamentoDTO dto = new AcompanhamentoDTO(
                            chamado.getStatus().name(),
                            localDateTime.toInstant(java.time.ZoneOffset.UTC) // Usando UTC como referência
                    );
                    log.info(String.format("Chamado encontrado para o token: %s", token));
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> {
                    log.warn(String.format("Nenhum chamado encontrado para o token: %s", token));
                    return ResponseEntity.notFound().build();
                });
    }
}