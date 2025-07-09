package com.example.login_auth_api.domain.user;

import com.example.login_auth_api.domain.user.User;
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

    @Enumerated(EnumType.STRING)
    private StatusChamado status;

    public enum StatusChamado {
        ABERTO,
        EM_ANDAMENTO,
        FECHADO,
        CANCELADO
    }

    @ManyToOne
    @JoinColumn(name = "userid") // Nome da coluna no banco de dados que guardará o ID do usuário técnico
    private User tecnico;

    // ... seus getters e setters

    public User getTecnico() {
        return tecnico;
    }

    public void setTecnico(User tecnico) {
        this.tecnico = tecnico;
    }

}
