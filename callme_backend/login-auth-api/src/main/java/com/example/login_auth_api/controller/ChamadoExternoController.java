package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.dto.AtualizarStatusDTO;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.dto.TecnicoDTO;
import com.example.login_auth_api.dto.TecnicoUpdateDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import com.example.login_auth_api.service.ChamadoExternoService;
import com.example.login_auth_api.service.EmailReceiverService;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        log.info("Criando chamados a partir de emails");

        List<EmailResumoDTO> resumos = emailReceiverService.listarResumosEmails();
        List<ChamadoExterno> chamados = chamadoService.criarChamadosAPartirDeEmails(resumos);

        log.info("Chamados criados com sucesso | Quantidade: " + chamados.size());
        return chamados;
    }

    @GetMapping("/listar")
    public List<ChamadoExterno> listarChamados() {
        log.info("Listando chamados");

        List<ChamadoExterno> chamados = chamadoService.listarChamados();

        log.info("Chamados listados | Quantidade: " + chamados.size());
        return chamados;
    }

    @GetMapping("/buscar/{tokenEmail}")
    public ResponseEntity<ChamadoExterno> buscarChamadoPorToken(@PathVariable String tokenEmail) {
        log.info("Buncando chamado por token | Token: " + tokenEmail);

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

        log.info("Editando chamado por token | Token: " + tokenEmail);

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
    public ResponseEntity<List<TecnicoDTO>> listarTecnicos() {
        log.info("Início: listar técnicos | Hora: " + getTimestamp());

        // Busca a lista de entidades User como antes
        List<User> tecnicosEntities = userRepository.findByRoles_Name("RT");

        // Converte a lista de User para uma lista de TecnicoDTO
        List<TecnicoDTO> tecnicosDTOs = tecnicosEntities.stream()
                // A correção está aqui, adicionando .toString() ao ID
                .map(user -> new TecnicoDTO(user.getUserid().toString(), user.getUsername()))
                .toList();

        log.info("Técnicos listados | Quantidade: " + tecnicosDTOs.size());

        // Retorna a lista de DTOs
        return ResponseEntity.ok(tecnicosDTOs);
    }

    @GetMapping("/meus-chamados")
    public ResponseEntity<List<ChamadoExterno>> getMeusChamados() {
        // Não precisamos do log aqui, pois o service já poderia logar se necessário
        List<ChamadoExterno> chamados = chamadoService.listarChamadosDoTecnicoLogado();
        return ResponseEntity.ok(chamados);
    }


    // Em: com/example/login_auth_api/controller/ChamadoExternoController.java

    @PutMapping("/{id}/tecnico-update")
    @PreAuthorize("hasAuthority('SCOPE_RT') or hasAuthority('SCOPE_ADMIN')") // Protege a rota
    public ResponseEntity<ChamadoExterno> atualizarPorTecnico(
            @PathVariable Long id,
            @RequestBody TecnicoUpdateDTO updateData) {

        Optional<ChamadoExterno> chamadoAtualizado = chamadoService.atualizarChamadoComoTecnico(id, updateData);

        return chamadoAtualizado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/finalizados")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_RT')")
    public ResponseEntity<List<ChamadoExterno>> getChamadosFinalizados() {
        List<ChamadoExterno> chamados = chamadoService.listarChamadosPorStatus(ChamadoExterno.StatusChamado.FECHADO);
        return ResponseEntity.ok(chamados);
    }

}