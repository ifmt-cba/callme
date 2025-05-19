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
    private EmailService emailService;

    // Cria chamados a partir da lista de resumos de email
    public List<ChamadoExterno> criarChamadosAPartirDeEmails(List<EmailResumoDTO> resumos) {
        return resumos.stream().map(resumo -> {
            // Evita duplicar chamados com o mesmo token do email
            return chamadoRepository.findByTokenEmail(resumo.getToken())
                    .orElseGet(() -> {
                        ChamadoExterno chamado = ChamadoExterno.builder()
                                .remetente(resumo.getRemetente())
                                .assunto(resumo.getAssunto())
                                .descricao(resumo.getCorpoSimples())
                                .dataHora(resumo.getDataHora())
                                .tokenEmail(resumo.getToken())
                                .build();

                        ChamadoExterno chamadoSalvo = chamadoRepository.save(chamado);

                        // Envia o e-mail de confirmação com o token
                        String emailLimpo = extractEmailAddress(resumo.getRemetente());
                        emailService.sendTokenResponse(emailLimpo, resumo.getToken());

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
}
