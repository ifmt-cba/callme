package com.example.login_auth_api.domain.user;

import com.example.login_auth_api.dto.LoginRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userid;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    // CORRIGIDO: Campo 'token' declarado apenas uma vez com sua anotação.
    @Column(unique = true)
    private String token;

    // CORRIGIDO: Campo 'password' com sua anotação.
    @JsonIgnore
    private String password;

    // CORRIGIDO: Campo 'resetTokenExpiration' com suas anotações,
    // o campo e os métodos getter/setter agrupados.
    @JsonIgnore
    @Column(name = "reset_token_expiration")
    private LocalDateTime resetTokenExpiration;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // --- GETTERS E SETTERS ---

    public UUID getUserid() {
        return userid;
    }

    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getResetTokenExpiration() {
        return resetTokenExpiration;
    }

    public void setResetTokenExpiration(LocalDateTime resetTokenExpiration) {
        this.resetTokenExpiration = resetTokenExpiration;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // --- MÉTODOS DE LÓGICA ---

    public boolean isLoginCorrect(LoginRequestDTO loginRequestDTO, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequestDTO.password(), this.password);
    }

    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isTokenExpired() {
        // Adicionando uma verificação para evitar NullPointerException
        if (this.resetTokenExpiration == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(this.resetTokenExpiration);
    }
}