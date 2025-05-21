package com.example.login_auth_api.service;

import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChamadoExternoService {

    @Autowired
    private ChamadoExternoRepository chamadoRepository;

    @Autowired
    private SendEmailService emailService;

    // Cria chamados a partir da lista de resumos de email
    public List<ChamadoExterno> criarChamadosAPartirDeEmails(List<EmailResumoDTO> resumos) {
        return resumos.stream().map(resumo -> {
            return chamadoRepository.findByMessageId(resumo.getMessageId())
                    .orElseGet(() -> {
                        ChamadoExterno chamado = ChamadoExterno.builder()
                                .remetente(resumo.getRemetente())
                                .assunto(resumo.getAssunto())
                                .descricao(resumo.getCorpoSimples())
                                .dataHora(resumo.getDataHora())
                                .tokenEmail(resumo.getToken())
                                .messageId(resumo.getMessageId())
                                .build();

                        ChamadoExterno chamadoSalvo = chamadoRepository.save(chamado);

                        // ✅ Envia e-mail de resposta ao cliente com o token
                        emailService.sendTokenResponse(
                                resumo.getRemetente(),
                                resumo.getToken(),
                                resumo.getMessageId(),
                                resumo.getAssunto()
                        );

                        return chamadoSalvo;
                    });

        }).collect(Collectors.toList());
    }


    public List<ChamadoExterno> listarChamados() {
        return chamadoRepository.findAll();
    }

    // Extrai o e-mail limpo de um campo tipo "Nome <email@exemplo.com>"
    private String extractEmailAddress(String rawRemetente) {
        if (rawRemetente == null) return "";
        if (rawRemetente.contains("<") && rawRemetente.contains(">")) {
            return rawRemetente.substring(rawRemetente.indexOf("<") + 1, rawRemetente.indexOf(">")).trim();
        }
        return rawRemetente.trim();
    }

    public ChamadoExterno atualizarStatus(Long chamadoId, ChamadoExterno.StatusChamado novoStatus) {
        ChamadoExterno chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        chamado.setStatus(novoStatus);
        return chamadoRepository.save(chamado);
    }

    public ChamadoExterno buscarPorId(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));
    }

    public ChamadoExterno atualizarStatusPorToken(String tokenEmail, String novoStatus) {
        ChamadoExterno chamado = chamadoRepository.findByTokenEmail(tokenEmail)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        try {
            ChamadoExterno.StatusChamado statusEnum = ChamadoExterno.StatusChamado.valueOf(novoStatus.toUpperCase());
            chamado.setStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + novoStatus);
        }

        return chamadoRepository.save(chamado);
    }



}
