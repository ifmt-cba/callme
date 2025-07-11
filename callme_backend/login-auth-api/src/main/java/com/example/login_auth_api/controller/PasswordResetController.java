package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.ForgotPasswordDTO;
import com.example.login_auth_api.repositories.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@PreAuthorize("permitAll()")
@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogPort log;

    public PasswordResetController(JavaMailSender mailSender,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   LogPort log) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.log = log;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();
        log.info(String.format("Solicitando senha esquecida | Email: %s", email));

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) {
            log.warn(String.format("Email não encontrado ao solicitar reset | Email: %s", email));
            return ResponseEntity.ok("Email não encontrado");
        }

        User user = userOpt.get();
        String token = user.generateResetToken();
        LocalDateTime expiration = LocalDateTime.now().plusHours(1);
        user.setToken(token);
        user.setResetTokenExpiration(expiration);
        userRepository.save(user);
        log.info(String.format("Reset token gerado e salvo | UserID: %s | Token expira em: %s", user.getUserid(), expiration));

        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        sendResetEmail(email, resetLink);
        log.info(String.format("Email de reset enviado com sucesso | Email: %s", email));

        return ResponseEntity.ok("Email enviado com sucesso");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @RequestParam String newPassword) {
        log.info(String.format("Resetando senha | Token: %s", token));

        Optional<User> userOpt = userRepository.findByToken(token);
        if (userOpt.isEmpty() || userOpt.get().isTokenExpired()) {
            log.warn(String.format("Token inválido ou expirado | Token: %s", token));
            return ResponseEntity.badRequest().body("Token inválido ou expirado");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setToken(null);
        user.setResetTokenExpiration(null);
        userRepository.save(user);

        log.info(String.format("Senha redefinida com sucesso | UserID: %s", user.getUserid()));
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    private void sendResetEmail(String email, String resetLink) {
        log.debug(String.format("Preparando email de reset | To: %s | Link: %s", email, resetLink));
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("seu_email@gmail.com");
            helper.setTo(email);
            helper.setSubject("🔐 Redefinição de Senha");

            String htmlContent = """
                <html>
                  <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                      <h2 style="color: #333;">Redefinição de Senha</h2>
                      <p>Olá,</p>
                      <p>Você solicitou a redefinição da sua senha. Clique no botão abaixo para redefinir:</p>
                      <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #2563eb; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;">Redefinir Senha</a>
                      </div>
                      <p>Se você não solicitou essa alteração, apenas ignore este e-mail.</p>
                      <p style="font-size: 12px; color: #888;">Este link é válido por 1 hora.</p>
                    </div>
                  </body>
                </html>
            """.formatted(resetLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.debug("Email de reset enviado pelo JavaMailSender");
        } catch (MessagingException e) {
            log.error("Falha ao enviar email de reset | Email: " + email + " | Erro: " + e.getMessage());
        }
    }
}
