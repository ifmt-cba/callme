package com.example.login_auth_api.service;

import com.example.login_auth_api.dto.EmailResumoDTO;
import com.example.login_auth_api.service.ChamadoExternoService;
import com.example.login_auth_api.service.EmailReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailSchedulerService {

    @Autowired
    private EmailReceiverService emailReceiverService;

    @Autowired
    private ChamadoExternoService chamadoExternoService;

    // Executa a cada 1 minuto (60_000 ms)
    @Scheduled(fixedRate = 60_000)
    public void processarEmailsPeriodicamente() {
        try {
            System.out.println("⏰ Agendador rodando: buscando emails...");

            // 1. Atualiza a base com novos emails (se houver)
            emailReceiverService.checkInbox();

            // 2. Lista resumos dos emails que foram armazenados
            List<EmailResumoDTO> resumos = emailReceiverService.listarResumosEmails();

            // 3. Cria chamados a partir dos resumos
            chamadoExternoService.criarChamadosAPartirDeEmails(resumos);

            System.out.println("✅ Chamados processados com sucesso.");
        } catch (Exception e) {
            System.err.println("❌ Erro ao processar chamados automaticamente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
