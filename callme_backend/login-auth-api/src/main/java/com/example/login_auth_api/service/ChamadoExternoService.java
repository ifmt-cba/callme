package com.example.login_auth_api.service;

import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.domain.user.Comentario;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.dto.TecnicoUpdateDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import com.example.login_auth_api.repositories.ComentarioRepository;
import com.example.login_auth_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChamadoExternoService {

    private final ChamadoExternoRepository chamadoRepository;
    private final UserRepository userRepository;
    private final SendEmailService emailService;
    private final ComentarioRepository comentarioRepository;

    public ChamadoExternoService(ChamadoExternoRepository chamadoRepository,
                                 UserRepository userRepository,
                                 SendEmailService emailService,
                                 ComentarioRepository comentarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.comentarioRepository = comentarioRepository;
    }

    @Transactional
    public List<ChamadoExterno> criarChamadosAPartirDeEmails(List<EmailResumoDTO> resumos) {
        return resumos.stream().map(resumo ->
                chamadoRepository.findByMessageId(resumo.getMessageId())
                        .orElseGet(() -> {
                            ChamadoExterno chamado = ChamadoExterno.builder()
                                    .remetente(resumo.getRemetente())
                                    .assunto(resumo.getAssunto())
                                    .descricao(resumo.getCorpoSimples())
                                    .dataHora(resumo.getDataHora())
                                    .tokenEmail(resumo.getToken())
                                    .messageId(resumo.getMessageId())
                                    // ✅ CORREÇÃO: Alterado de NOVO para ABERTO
                                    .status(ChamadoExterno.StatusChamado.ABERTO)
                                    .build();

                            ChamadoExterno chamadoSalvo = chamadoRepository.save(chamado);

                            emailService.sendTokenResponse(
                                    resumo.getRemetente(),
                                    resumo.getToken(),
                                    resumo.getMessageId(),
                                    resumo.getAssunto()
                            );
                            return chamadoSalvo;
                        })
        ).collect(Collectors.toList());
    }

    // ... (O resto do seu código continua igual)

    @Transactional(readOnly = true)
    public List<ChamadoExterno> listarChamados() {
        return chamadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ChamadoExterno> buscarChamadoPorToken(String tokenEmail) {
        return chamadoRepository.findByTokenEmail(tokenEmail);
    }

    @Transactional
    public Optional<ChamadoExterno> editarChamadoPorToken(String tokenEmail, ChamadoExterno dadosDoFrontend) {
        return chamadoRepository.findByTokenEmail(tokenEmail).map(chamadoDoBanco -> {
            chamadoDoBanco.setStatus(dadosDoFrontend.getStatus());
            if (dadosDoFrontend.getTecnico() != null && dadosDoFrontend.getTecnico().getUserid() != null) {
                UUID tecnicoId = dadosDoFrontend.getTecnico().getUserid();
                User tecnicoGerenciado = userRepository.findById(tecnicoId)
                        .orElseThrow(() -> new EntityNotFoundException("Técnico com ID " + tecnicoId + " não encontrado."));
                chamadoDoBanco.setTecnico(tecnicoGerenciado);
            } else {
                chamadoDoBanco.setTecnico(null);
            }
            return chamadoDoBanco;
        });
    }

    @Transactional(readOnly = true)
    public List<ChamadoExterno> listarChamadosDoTecnicoLogado() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = UUID.fromString(principal.getSubject());
        User tecnicoLogado = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + userId + " do token não foi encontrado."));
        return chamadoRepository.findByTecnicoAndStatusIsNot(tecnicoLogado, ChamadoExterno.StatusChamado.FECHADO);
    }

    @Transactional
    public Optional<ChamadoExterno> atualizarChamadoComoTecnico(Long chamadoId, TecnicoUpdateDTO updateData) {
        // Pega o técnico logado (esta parte está correta)
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = UUID.fromString(principal.getSubject());
        User tecnicoLogado = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Técnico do token não encontrado no banco"));

        return chamadoRepository.findById(chamadoId).map(chamadoDoBanco -> {
            // Validação de segurança
            if (chamadoDoBanco.getTecnico() == null || !chamadoDoBanco.getTecnico().getUserid().equals(tecnicoLogado.getUserid())) {
                throw new AccessDeniedException("Você não tem permissão para editar este chamado.");
            }

            // 1. Guarda o novo status em uma variável
            ChamadoExterno.StatusChamado novoStatus = ChamadoExterno.StatusChamado.valueOf(updateData.status());

            // 2. Atualiza o status do chamado
            chamadoDoBanco.setStatus(novoStatus);

            // 3. SE o novo status for FECHADO, grava a data de finalização
            if (novoStatus == ChamadoExterno.StatusChamado.FECHADO) {
                chamadoDoBanco.setDataFinalizacao(LocalDateTime.now());
            }

            // 4. Adiciona o novo comentário (lógica que já funciona)
            if (updateData.comentario() != null && !updateData.comentario().isBlank()) {
                Comentario novoComentario = new Comentario(updateData.comentario(), chamadoDoBanco, tecnicoLogado);
                chamadoDoBanco.getComentarios().add(novoComentario);
            }

            return chamadoDoBanco;
        });
    }

    @Transactional(readOnly = true)
    public List<ChamadoExterno> listarChamadosPorStatus(ChamadoExterno.StatusChamado status) {
        return chamadoRepository.findByStatus(status);
    }

    @Transactional
    public Optional<ChamadoExterno> atualizarStatus(Long chamadoId, ChamadoExterno.StatusChamado novoStatus) {
        return chamadoRepository.findById(chamadoId).map(chamado -> {
            chamado.setStatus(novoStatus);
            return chamado;
        });
    }
}