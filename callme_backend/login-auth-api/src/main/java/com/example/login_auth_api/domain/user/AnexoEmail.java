package com.example.login_auth_api.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@Table(name = "anexos_email")
public class AnexoEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;
    private String tipoMime;

    @Lob
    private byte[] conteudo;

    private LocalDateTime dataRecebimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id") // Adiciona a chave estrangeira
    private SendEmail email; // Relacionamento com o email
}

