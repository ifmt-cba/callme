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

    public void setChamadoID(long chamadoID) {
        this.chamadoID = chamadoID;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

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
