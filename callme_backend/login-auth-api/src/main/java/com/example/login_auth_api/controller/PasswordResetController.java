package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.ForgotPasswordDTO;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@PreAuthorize("permitAll()")
@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("permitAll()")
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();

        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isEmpty()) {
            return ResponseEntity.ok("Email nao encontrado");
        }

        User currentUser = user.get();
        String token = currentUser.generateResetToken();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
        currentUser.setToken(token);
        currentUser.setResetTokenExpiration(expirationTime);

        userRepository.save(currentUser);

        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        sendResetEmail(email, resetLink);

        return ResponseEntity.ok("email enviado com sucesso");
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/reset")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        Optional<User> user = userRepository.findByToken(token);

        if (user.isEmpty() || user.get().isTokenExpired()) {
            return "Token inválido ou expirado";
        }

        User currentUser = user.get();
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        currentUser.setToken(null);
        currentUser.setResetTokenExpiration(null);

        userRepository.save(currentUser);

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
