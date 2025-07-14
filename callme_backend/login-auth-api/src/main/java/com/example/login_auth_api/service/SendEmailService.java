package com.example.login_auth_api.service;


import com.example.login_auth_api.dto.SendEmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
    private final JavaMailSender mailSender;

    public SendEmailService(JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }
    public void sendEmail(SendEmailDTO email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("callmegerencia@gmail.com");
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());
        mailSender.send(message);

    }

    public void sendTokenResponse(String to, String token, String originalMessageId, String originalSubject) {
        String subject = "Re: " + originalSubject;

        String body = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
                <div style="background-color: white; padding: 20px; border-radius: 8px;">
                    <p>Olá,</p>
                    <p>Recebemos sua solicitação com sucesso. Seu número de acompanhamento é:</p>
                    <p style="font-size: 18px; font-weight: bold; color: #2b6777;">%s</p>
                    <p>Entraremos em contato em breve.</p>
                    <p style="margin-top: 20px;">Atenciosamente,<br>Equipe CallMe</p>
                </div>
            </body>
            </html>
            """.formatted(token);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("callmegerencia@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            // Adiciona cabeçalhos de resposta
            message.setHeader("In-Reply-To", originalMessageId);
            message.setHeader("References", originalMessageId);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar resposta com token", e);
        }
    }

    public void sendChamadoFinalizadoResponse(String to, String originalMessageId, String originalSubject) {
        String subject = "Re: " + originalSubject + " (Resolvido)";

        String body = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
                <div style="background-color: white; padding: 20px; border-radius: 8px;">
                    <p>Olá,</p>
                    <p>Sua solicitação referente ao chamado "%s" foi concluída pela nossa equipe.</p>
                    <p>Se o problema persistir ou se tiver uma nova solicitação, por favor, responda a este e-mail ou crie um novo chamado.</p>
                    <p style="margin-top: 20px;">Atenciosamente,<br>Equipe CallMe</p>
                </div>
            </body>
            </html>
            """.formatted(originalSubject);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("callmegerencia@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            // Adiciona cabeçalhos para que o email apareça como uma resposta na thread original
            message.setHeader("In-Reply-To", originalMessageId);
            message.setHeader("References", originalMessageId);

            mailSender.send(message);
        } catch (MessagingException e) {
            // Lançar uma exceção mais específica ou logar o erro
            throw new RuntimeException("Erro ao enviar e-mail de finalização", e);
        }
    }




}
