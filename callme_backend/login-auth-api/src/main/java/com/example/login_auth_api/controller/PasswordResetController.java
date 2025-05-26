package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.ForgotPasswordDTO;
import com.example.login_auth_api.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
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

            helper.setText(htmlContent, true); // HTML mode
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
