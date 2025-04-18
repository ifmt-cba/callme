package com.example.login_auth_api.service;


import com.example.login_auth_api.dto.Email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }
    public void sendEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("callmegerencia@gmail.com");
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());
        mailSender.send(message);

    }
}
