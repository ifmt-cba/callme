package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.ChamadoInterno;
import com.example.login_auth_api.domain.user.Role;
import com.example.login_auth_api.dto.ChamadoInternoDto;
import com.example.login_auth_api.dto.FeedDto;
import com.example.login_auth_api.dto.FeedItemDto;
import com.example.login_auth_api.repositories.ChamadoInternoRepository;
import com.example.login_auth_api.repositories.UserRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class ChamadoInternoController {

    private final ChamadoInternoRepository chamadoRepository;
    private final UserRepository userRepository;
    private final LogPort log;

    public ChamadoInternoController(ChamadoInternoRepository chamadoRepository, UserRepository userRepository, LogPort log) {
        this.chamadoRepository = chamadoRepository;
        this.userRepository = userRepository;
        this.log = log;
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        log.info(String.format("Listagem de feed iniciada | Page: %d | PageSize: %d", page, pageSize));

        var chamados = chamadoRepository.findAll(
                PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(chamado ->
                        new FeedItemDto(chamado.getChamadoID(),
                                chamado.getContent(),
                                chamado.getUser().getUsername()));

        log.info(String.format("Feed retornado com %d itens", chamados.getNumberOfElements()));

        return ResponseEntity.ok(new FeedDto(chamados.getContent(), page, pageSize,
                chamados.getTotalPages(),
                chamados.getTotalElements()));
    }

    @PostMapping("/chamados")
    public ResponseEntity<Void> createChamado(@RequestBody ChamadoInternoDto dto,
                                              JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(token.getName());
        log.info(String.format("Criando chamado | UserID: %s", userId));

        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Usuário não encontrado para criação de chamado | UserID: " + userId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var chamado = new ChamadoInterno();
        chamado.setUser(user.get());
        chamado.setContent(dto.content());

        chamadoRepository.save(chamado);

        log.info(String.format("Chamado criado com sucesso | UserID: %s | ChamadoID: %d", userId, chamado.getChamadoID()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/chamados/{id}")
    public ResponseEntity<Void> deleteChamado(@PathVariable("id") Long chamadoId,
                                              JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(token.getName());
        log.info(String.format("Deletando chamado | ChamadoID: %d | UserID: %s", chamadoId, userId));

        var user = userRepository.findById(userId);
        var chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> {
                    log.warn("Chamado não encontrado para deleção | ChamadoID: " + chamadoId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        var isAdmin = user.get().getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (isAdmin || chamado.getUser().getUserid().equals(userId)) {
            chamadoRepository.deleteById(chamadoId);
            log.info(String.format("Chamado deletado com sucesso | ChamadoID: %d | UserID: %s", chamadoId, userId));
            return ResponseEntity.ok().build();
        } else {
            log.warn(String.format("Acesso negado para deletar chamado | ChamadoID: %d | UserID: %s", chamadoId, userId));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}