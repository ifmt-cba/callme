package com.example.login_auth_api.service;

import com.example.login_auth_api.domain.user.Comentario;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.dto.TecnicoUpdateDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import com.example.login_auth_api.repositories.ComentarioRepository;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
@Service
public class ChamadoExternoService {

    @Autowired
    private ChamadoExternoRepository chamadoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmailService emailService;

    @Autowired
    private ComentarioRepository comentarioRepository; // <-- INJETE O NOVO REPOSITÓRIO


    @Transactional(readOnly = true)
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


    @Transactional(readOnly = true) // Adiciona transação (apenas leitura, mais otimizado)
    public List<ChamadoExterno> listarChamados() {
        System.out.println("\n--- [SERVICE LOG] Iniciando listarChamados() ---");
        List<ChamadoExterno> chamados = chamadoRepository.findAll();
        System.out.println("--> Encontrados " + chamados.size() + " chamados no total.");
        System.out.println("--- [SERVICE LOG] Fim do método listarChamados(). ---");
        return chamados;
    }

    private String extractEmailAddress(String rawRemetente) {
        if (rawRemetente == null) return "";
        if (rawRemetente.contains("<") && rawRemetente.contains(">")) {
            return rawRemetente.substring(rawRemetente.indexOf("<") + 1, rawRemetente.indexOf(">")).trim();
        }
        return rawRemetente.trim();
    }
    @Transactional(readOnly = true) // Adiciona transação (apenas leitura, mais otimizado)
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

    @Transactional(readOnly = true) // Adiciona transação (apenas leitura)
    public List<ChamadoExterno> listarChamadosDoTecnicoLogado() {
        System.out.println("\n--- [SERVICE LOG] Iniciando listarChamadosDoTecnicoLogado ---");

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        String userIdAsString = principal.getSubject();
        UUID userId = UUID.fromString(userIdAsString);
        System.out.println("--> Buscando usuário com ID do token: " + userId);

        User tecnicoLogado = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + userId + " do token não foi encontrado."));
        System.out.println("--> Técnico encontrado: " + tecnicoLogado.getUsername());

        System.out.println("--> Buscando chamados atribuídos a este técnico...");
        List<ChamadoExterno> chamados = chamadoRepository.findByTecnico(tecnicoLogado);
        System.out.println("--> Encontrados " + chamados.size() + " chamados para este técnico.");
        System.out.println("--- [SERVICE LOG] Fim do método listarChamadosDoTecnicoLogado(). ---");

        return chamados;
    }


    @Transactional
    public Optional<ChamadoExterno> atualizarChamadoComoTecnico(Long chamadoId, TecnicoUpdateDTO updateData) {
        // 1. Pega a autenticação atual
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Extrai o objeto Jwt do principal
        Jwt principal = (Jwt) authentication.getPrincipal();

        // 3. Pega o "subject" do token (que é o ID do usuário em formato String)
        String userIdString = principal.getSubject();
        UUID userId = UUID.fromString(userIdString);

        // 4. Usa o ID do token para buscar o técnico logado no banco de dados
        User tecnicoLogado = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Técnico do token não encontrado no banco"));

        // 5. Agora busca o chamado que será editado
        return chamadoRepository.findById(chamadoId).map(chamadoDoBanco -> {

            // 6. VERIFICAÇÃO DE SEGURANÇA: Garante que o chamado pertence ao técnico logado
            if (chamadoDoBanco.getTecnico() == null || !chamadoDoBanco.getTecnico().getUserid().equals(tecnicoLogado.getUserid())) {
                throw new AccessDeniedException("Você não tem permissão para editar este chamado.");
            }

            // 7. Atualiza o status
            chamadoDoBanco.setStatus(ChamadoExterno.StatusChamado.valueOf(updateData.status()));

            // 8. Adiciona o novo comentário
            if (updateData.comentario() != null && !updateData.comentario().isBlank()) {
                Comentario novoComentario = new Comentario(updateData.comentario(), chamadoDoBanco, tecnicoLogado);
                chamadoDoBanco.getComentarios().add(novoComentario);
            }

            // A transação cuidará de salvar as alterações
            return chamadoDoBanco;
        });
    }
}





