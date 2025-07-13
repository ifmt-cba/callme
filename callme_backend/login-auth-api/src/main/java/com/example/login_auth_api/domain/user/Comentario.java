package com.example.login_auth_api.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_comentarios")
@Getter
@Setter
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob // Para textos longos
    private String texto;

    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "chamado_id", nullable = false)
    @JsonIgnore // Evita loops de serialização
    private ChamadoExterno chamado;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private User autor; // O técnico que escreveu o comentário

    public Comentario(String texto, ChamadoExterno chamado, User autor) {
        this.texto = texto;
        this.chamado = chamado;
        this.autor = autor;
        this.dataCriacao = LocalDateTime.now();
    }
}