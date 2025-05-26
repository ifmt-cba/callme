package com.example.login_auth_api.domain.user;

import com.example.login_auth_api.dto.LoginRequestDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GeneratorType;
import org.springframework.boot.autoconfigure.web.WebProperties;
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

    @Column(unique = true)
    private String token;

    private String password;

    public LocalDateTime getResetTokenExpiration() {
        return resetTokenExpiration;
    }

    public void setResetTokenExpiration(LocalDateTime resetTokenExpiration) {
        this.resetTokenExpiration = resetTokenExpiration;
    }

    @Column(name = "reset_token_expiration")
    private LocalDateTime resetTokenExpiration;


    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(

            name =  "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // Aqui e feito a comparacao das senhas do DTO e do Encoder
    public boolean isLoginCorrect(LoginRequestDTO loginRequestDTO, PasswordEncoder passwordEncoder) {

       return passwordEncoder.matches(loginRequestDTO.password(), this.password);


    }

    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isTokenExpired() {
        return LocalDateTime.now().isAfter(this.resetTokenExpiration);
    }


}
