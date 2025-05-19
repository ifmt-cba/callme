package com.example.login_auth_api.service;


import com.example.login_auth_api.dto.SendEmailDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {

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

    public void sendTokenResponse(String to, String token) {
        String subject = "Confirmação de recebimento do chamado";
        String body = "Olá,\n\nRecebemos sua solicitação com sucesso.\n" +
                "Seu número de acompanhamento (token) é: " + token + "\n\n" +
                "Entraremos em contato em breve.\n\n" +
                "Atenciosamente,\nEquipe CallMe";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("callmegerencia@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
