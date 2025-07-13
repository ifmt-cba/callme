package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.ChamadoInterno;
import com.example.login_auth_api.domain.user.Role;
import com.example.login_auth_api.dto.ChamadoInternoDto;
import com.example.login_auth_api.dto.ChamadoUnificadoDTO;
import com.example.login_auth_api.dto.FeedDto;
import com.example.login_auth_api.dto.FeedItemDto;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import com.example.login_auth_api.repositories.ChamadoInternoRepository;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
public class ChamadoInternoController {

    private final ChamadoInternoRepository chamadoInternoRepository;
    private final ChamadoExternoRepository chamadoExternoRepository;
    private final UserRepository userRepository;
    private final LogPort log;

    public ChamadoInternoController(ChamadoInternoRepository chamadoInternoRepository,
                                    ChamadoExternoRepository chamadoExternoRepository,
                                    UserRepository userRepository,
                                    LogPort log) {
        this.chamadoInternoRepository = chamadoInternoRepository;
        this.chamadoExternoRepository = chamadoExternoRepository;
        this.userRepository = userRepository;
        this.log = log;
    }

    @PostMapping("/chamados")
    public ResponseEntity<Void> createChamado(@RequestBody ChamadoInternoDto dto,
                                              JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(token.getName());
        log.info(String.format("Criando chamado | UserID: %s", userId));

        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var chamado = new ChamadoInterno();
        chamado.setUser(user.get());
        chamado.setContent(dto.content());
        // Aqui você adicionaria a descrição, se ela vier no DTO
        // chamado.setDescricao(dto.descricao());

        chamadoInternoRepository.save(chamado);
        log.info(String.format("Chamado criado com sucesso | UserID: %s | ChamadoID: %d", userId, chamado.getChamadoID()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        log.info(String.format("Listagem de feed iniciada | Page: %d | PageSize: %d", page, pageSize));

        var chamados = chamadoInternoRepository.findAll(
                        PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(chamado ->
                        new FeedItemDto(chamado.getChamadoID(),
                                chamado.getContent(),
                                chamado.getUser().getUsername()));

        return ResponseEntity.ok(new FeedDto(chamados.getContent(), page, pageSize,
                chamados.getTotalPages(),
                chamados.getTotalElements()));
    }

    @DeleteMapping("/chamados/{id}")
    public ResponseEntity<Void> deleteChamado(@PathVariable("id") Long chamadoId,
                                              JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(token.getName());
        log.info(String.format("Deletando chamado | ChamadoID: %d | UserID: %s", chamadoId, userId));

        var user = userRepository.findById(userId);
        var chamado = chamadoInternoRepository.findById(chamadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (isAdmin || chamado.getUser().getUserid().equals(userId)) {
            chamadoInternoRepository.deleteById(chamadoId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/chamados-unificados")
    public ResponseEntity<List<ChamadoUnificadoDTO>> getChamadosUnificados() {
        log.info("Buscando chamados unificados para a home page.");

        Stream<ChamadoUnificadoDTO> internosStream = chamadoInternoRepository.findAll().stream()
                .map(chamado -> new ChamadoUnificadoDTO(
                        chamado.getChamadoID(),
                        "INTERNO",
                        chamado.getContent(),
                        chamado.getDescricao(),
                        chamado.getUser().getUsername(),
                        chamado.getCreationTimestamp()
                ));

        Stream<ChamadoUnificadoDTO> externosStream = chamadoExternoRepository.findAll().stream()
                .map(chamado -> new ChamadoUnificadoDTO(
                        chamado.getId(),
                        "EXTERNO",
                        chamado.getAssunto(),
                        chamado.getDescricao(),
                        chamado.getRemetente(),
                        Instant.now() // Ajuste se houver campo de data no chamado externo
                ));

        List<ChamadoUnificadoDTO> chamadosUnificados = Stream.concat(internosStream, externosStream)
                .sorted(Comparator.comparing(ChamadoUnificadoDTO::data).reversed())
                .toList();

        return ResponseEntity.ok(chamadosUnificados);
    }
}