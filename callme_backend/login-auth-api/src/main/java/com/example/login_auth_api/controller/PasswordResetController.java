package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.ForgotPasswordDTO;
import com.example.login_auth_api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;  // Repositório para buscar o usuário

    @Autowired
    private PasswordEncoder passwordEncoder;  // Para encriptar a senha

    // Endpoint para solicitar reset de senha
    @Transactional
    @PostMapping("/forgot")
    public String forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        String email = dto.getEmail().trim().toLowerCase(); // Evita espaços e diferenças de caixa

        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isEmpty()) {
            return "E-mail não encontrado";
        }

        User currentUser = user.get();
        String token = currentUser.generateResetToken();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
        currentUser.setToken(token);
        currentUser.setResetTokenExpiration(expirationTime);

        userRepository.save(currentUser);

        String resetLink = "http://localhost:8080/api/password/reset?token=" + token;
        sendResetEmail(email, resetLink);

        return "E-mail de reset enviado";
    }

    // Endpoint para resetar a senha
    @PostMapping("/reset")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        Optional<User> user = userRepository.findByToken(token);

        if (user.isEmpty() || user.get().isTokenExpired()) {
            return "Token inválido ou expirado";
        }

        User currentUser = user.get();
        currentUser.setPassword(passwordEncoder.encode(newPassword));  // Encriptar nova senha
        currentUser.setToken(null);  // Limpar o token após a alteração
        currentUser.setResetTokenExpiration(null);  // Limpar a expiração

        userRepository.save(currentUser);  // Salvar a senha alterada

        return "Senha alterada com sucesso!";
    }

    private void sendResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("seu_email@gmail.com");
        message.setTo(email);
        message.setSubject("Redefinição de Senha");
        message.setText("Você solicitou a redefinição de sua senha. Clique no link abaixo para redefinir:\n" + resetLink);

        mailSender.send(message);
    }
}
