package com.example.login_auth_api.domain.user;

import com.example.login_auth_api.domain.user.User;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_chamadointerno")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "chamado_id")
    private long chamadoID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public long getChamadoID() {
        return chamadoID;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    private String content;

    @CreationTimestamp
    private Instant creationTimestamp;




}
