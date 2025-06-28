package com.example.login_auth_api.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_email_inbox")
public class SendEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remetente;
    private String destinatario;
    private String assunto;

    @Column(length = 5000)
    private String corpoSimples;

    private String dataHora;

    private String messageId;

    private String dkim;
    private String dmarc;
    private String spf;

    @Column(length = 10000)
    private String comprovante;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AnexoEmail> anexos; // Relacionamento com os anexos
}

