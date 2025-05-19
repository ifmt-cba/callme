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
                        return chamadoRepository.save(chamado);
                    });
        }).collect(Collectors.toList());
    }

    public List<ChamadoExterno> listarChamados() {
        return chamadoRepository.findAll();
    }

}
