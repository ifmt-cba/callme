// Em: src/main/java/com/example/login_auth_api/controller/AcompanhamentoController.java
package com.example.login_auth_api.controller;

import org.springframework.transaction.annotation.Transactional; // <-- ADICIONE ESTE IMPORT
import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.dto.AcompanhamentoDTO;
import com.example.login_auth_api.dto.ComentarioDTO; // Supondo que você tenha um ComentarioDTO
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/acompanhamentos") // A rota base para este controller
public class AcompanhamentoController {

    private final ChamadoExternoRepository chamadoExternoRepository;
    private final LogPort log;

    public AcompanhamentoController(ChamadoExternoRepository chamadoExternoRepository, LogPort log) {
        this.chamadoExternoRepository = chamadoExternoRepository;
        this.log = log;
    }

    // O frontend está chamando esta rota
    @GetMapping("/{token}")
    @Transactional(readOnly = true) // <-- ADICIONE A ANOTAÇÃO AQUI
    public ResponseEntity<AcompanhamentoDTO> getAcompanhamentoPorToken(@PathVariable String token) {
        log.info(String.format("Buscando acompanhamento público para o token: %s", token));

        return chamadoExternoRepository.findByTokenEmail(token)
                .map(chamado -> {
                    // Mapeia a entidade para um DTO de resposta para o cliente
                    AcompanhamentoDTO dto = new AcompanhamentoDTO(
                            chamado.getStatus().name(),
                            chamado.getDataHora(),
                            // Mapeia a lista de comentários para uma lista de DTOs de comentário
                            chamado.getComentarios().stream()
                                    .map(comentario -> new ComentarioDTO(
                                            comentario.getTexto(),
                                            comentario.getDataCriacao().toString(),
                                            comentario.getAutor().getUsername()
                                    ))
                                    .collect(Collectors.toList())
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