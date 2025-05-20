package com.example.login_auth_api.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_chamados_Externos")
public class ChamadoExterno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remetente;
    private String assunto;
    @Column(length = 2000)
    private String descricao;  // corpo do email resumido
    private String dataHora;
    private String tokenEmail;// token do email para evitar duplicidade
    private String messageId;

}
