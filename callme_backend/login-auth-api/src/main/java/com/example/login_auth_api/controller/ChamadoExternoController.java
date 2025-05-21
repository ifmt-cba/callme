package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.dto.AtualizarStatusDTO;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.service.ChamadoExternoService;
import com.example.login_auth_api.service.EmailReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/listar")
    public List<ChamadoExterno> listarChamados() {
        return chamadoService.listarChamados();
    }

    @PatchMapping("/status")
    public ResponseEntity<String> atualizarStatus(@RequestBody AtualizarStatusDTO dto) {
        chamadoService.atualizarStatus(dto.getChamadoId(), dto.getNovoStatus());
        return ResponseEntity.ok("Status atualizado com sucesso.");
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ChamadoExterno.StatusChamado> consultarStatus(@PathVariable Long id) {
        ChamadoExterno chamado = chamadoService.buscarPorId(id);
        return ResponseEntity.ok(chamado.getStatus());
    }

    @PutMapping("/{tokenEmail}/status")
    public ResponseEntity<ChamadoExterno> atualizarStatus(
            @PathVariable String tokenEmail,
            @RequestBody Map<String, String> statusPayload) {

        String novoStatus = statusPayload.get("status");
        if (novoStatus == null || novoStatus.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ChamadoExterno atualizado = chamadoService.atualizarStatusPorToken(tokenEmail, novoStatus);
        return ResponseEntity.ok(atualizado);
    }




}
