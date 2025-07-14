package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.AtualizarStatusDTO;
import com.example.login_auth_api.dto.TecnicoDTO;
import com.example.login_auth_api.dto.TecnicoUpdateDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import com.example.login_auth_api.repositories.UserRepository;
import com.example.login_auth_api.service.ChamadoExternoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chamados")
public class ChamadoExternoController {

    private final ChamadoExternoService chamadoService;
    private final UserRepository userRepository;
    private final LogPort log;
    private final ChamadoExternoRepository chamadoExternoRepository;

    // Construtor limpo com as dependências necessárias
    public ChamadoExternoController(ChamadoExternoService chamadoService,
                                    UserRepository userRepository,
                                    LogPort log, ChamadoExternoRepository chamadoExternoRepository) {
        this.chamadoService = chamadoService;
        this.userRepository = userRepository;
        this.log = log;
        this.chamadoExternoRepository = chamadoExternoRepository;
    }

    @GetMapping("/listar")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_RT')")
    public ResponseEntity<List<ChamadoExterno>> listarChamados() {
        log.info("Listando todos os chamados externos.");
        List<ChamadoExterno> chamados = chamadoService.listarChamados();
        return ResponseEntity.ok(chamados);
    }

    @GetMapping("/buscar/{tokenEmail}")
    @Transactional(readOnly = true)
    public ResponseEntity<ChamadoExterno> buscarChamadoPorToken(@PathVariable String tokenEmail) {
        log.info("Buscando chamado por token: " + tokenEmail);
        return chamadoService.buscarChamadoPorToken(tokenEmail)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/editar/token/{tokenEmail}")
    @Transactional
    public ResponseEntity<ChamadoExterno> editarChamadoPorToken(
            @PathVariable String tokenEmail,
            @RequestBody ChamadoExterno chamadoAtualizado) {
        log.info("Editando chamado por token: " + tokenEmail);
        return chamadoService.editarChamadoPorToken(tokenEmail, chamadoAtualizado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tecnicos")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<TecnicoDTO>> listarTecnicos() {
        log.info("Listando técnicos (RT).");
        List<TecnicoDTO> tecnicos = userRepository.findByRoles_Name("RT").stream()
                .map(user -> new TecnicoDTO(user.getUserid().toString(), user.getUsername()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(tecnicos);
    }

    @GetMapping("/meus-chamados")
    @PreAuthorize("hasAuthority('SCOPE_RT')") // Garante que apenas técnicos podem acessar
    public ResponseEntity<List<ChamadoExterno>> getMeusChamados() {
        log.info("Buscando chamados para o técnico logado.");
        List<ChamadoExterno> chamados = chamadoService.listarChamadosDoTecnicoLogado();
        return ResponseEntity.ok(chamados);
    }

    @PutMapping("/{id}/tecnico-update")
    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_RT')")
    public ResponseEntity<ChamadoExterno> atualizarPorTecnico(
            @PathVariable Long id,
            @RequestBody TecnicoUpdateDTO updateData) {
        log.info("Técnico atualizando chamado ID: " + id);
        return chamadoService.atualizarChamadoComoTecnico(id, updateData)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/finalizados")
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_RT')")
    public ResponseEntity<List<ChamadoExterno>> getChamadosFinalizados() {
        log.info("Listando chamados finalizados.");
        List<ChamadoExterno> chamados = chamadoService.listarChamadosPorStatus(ChamadoExterno.StatusChamado.FECHADO);
        return ResponseEntity.ok(chamados);
    }

    // ✅ ENDPOINT CORRIGIDO PARA USAR SEU DTO EXISTENTE
    @PutMapping("/atualizar-status")
    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_RT')")
    public ResponseEntity<ChamadoExterno> atualizarStatus(@RequestBody AtualizarStatusDTO dto) {
        log.info("Atualizando status do chamado ID: " + dto.getChamadoId() + " para " + dto.getNovoStatus());

        // Assumindo que seu serviço tem um método que aceita (Long, StatusChamado)
        return chamadoService.atualizarStatus(dto.getChamadoId(), dto.getNovoStatus())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}