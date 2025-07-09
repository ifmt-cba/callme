package com.example.login_auth_api.service;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
public class ChamadoExternoService {

    @Autowired
    private ChamadoExternoRepository chamadoRepository;

    @Autowired
    private UserRepository userRepository;

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

    public Optional<ChamadoExterno> buscarChamadoPorToken(String tokenEmail) {
        return chamadoRepository.findByTokenEmail(tokenEmail);
    }

    // Em: com/example/login_auth_api/service/ChamadoExternoService.java

    @Transactional
    public Optional<ChamadoExterno> editarChamadoPorToken(String tokenEmail, ChamadoExterno dadosDoFrontend) {
        // Log 1: Ponto de entrada do método
        System.out.println("\n--- [SERVICE LOG] Iniciando editarChamadoPorToken ---");
        System.out.println(">>> Token do Chamado Recebido: " + tokenEmail);

        // Log 2: Verifica os dados recebidos do frontend
        if (dadosDoFrontend != null) {
            System.out.println(">>> Dados do Frontend: Status=" + dadosDoFrontend.getStatus());
            if (dadosDoFrontend.getTecnico() != null && dadosDoFrontend.getTecnico().getUserid() != null) {
                System.out.println(">>> Dados do Frontend: ID do Tecnico=" + dadosDoFrontend.getTecnico().getUserid());
            } else {
                System.out.println(">>> Dados do Frontend: Tecnico está nulo ou sem ID.");
            }
        } else {
            System.out.println(">>> ERRO: O objeto 'chamadoAtualizado' vindo do frontend é NULO!");
        }

        // Busca o chamado no banco
        return chamadoRepository.findByTokenEmail(tokenEmail).map(chamadoDoBanco -> {
            // Log 3: Confirma que o chamado foi encontrado
            System.out.println("\n--- [SERVICE LOG] Chamado encontrado no banco. ID: " + chamadoDoBanco.getId() + " ---");
            System.out.println(">>> Status ANTES: " + chamadoDoBanco.getStatus());
            System.out.println(">>> Tecnico ANTES: " + (chamadoDoBanco.getTecnico() != null ? chamadoDoBanco.getTecnico().getUsername() : "Nenhum"));

            // 2. Atualiza o status
            chamadoDoBanco.setStatus(dadosDoFrontend.getStatus());
            System.out.println(">>> Status DEPOIS: " + chamadoDoBanco.getStatus());

            // 3. Lógica para atualizar o técnico
            // Verifica se o frontend enviou um técnico com um ID válido
            if (dadosDoFrontend.getTecnico() != null && dadosDoFrontend.getTecnico().getUserid() != null) {
                System.out.println(">>> [BLOCO IF] Tentando atribuir técnico...");
                UUID tecnicoId = dadosDoFrontend.getTecnico().getUserid();
                System.out.println("--> Buscando no UserRepository com ID: " + tecnicoId);

                User tecnicoGerenciado = userRepository.findById(tecnicoId)
                        .orElseThrow(() -> new EntityNotFoundException("Técnico com ID " + tecnicoId + " não encontrado."));

                System.out.println("--> Técnico encontrado no banco: " + tecnicoGerenciado.getUsername());
                chamadoDoBanco.setTecnico(tecnicoGerenciado);
                System.out.println(">>> Tecnico DEPOIS: " + chamadoDoBanco.getTecnico().getUsername());

            } else {
                System.out.println(">>> [BLOCO ELSE] Removendo atribuição de técnico...");
                chamadoDoBanco.setTecnico(null);
                System.out.println(">>> Tecnico DEPOIS: Nenhum");
            }

            System.out.println("\n--- [SERVICE LOG] Fim do método. A transação deve ser comitada agora. ---");
            return chamadoDoBanco;
        });
    }



}

