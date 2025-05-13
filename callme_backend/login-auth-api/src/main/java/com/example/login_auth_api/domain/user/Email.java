package com.example.login_auth_api.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remetente;

    private String assunto;

    @Column(length = 5000)
    private String corpoSimples;

    @Column(length = 10000)
    private String comprovante;

    private String messageId;
}
