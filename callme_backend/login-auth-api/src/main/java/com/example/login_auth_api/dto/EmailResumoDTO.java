package com.example.login_auth_api.dto;
import lombok.Data;

@Data
public class EmailResumoDTO {
    private String remetente;
    private String assunto;
    private String corpoSimples;
    private String dataHora;
    private String token;
    private String messageId;

    public EmailResumoDTO(String remetente, String assunto, String corpoSimples, String dataHora, String token, String messageId) {
        this.remetente = remetente;
        this.assunto = assunto;
        this.corpoSimples = corpoSimples;
        this.dataHora = dataHora;
        this.token = token;
        this.messageId = messageId;
    }


    // Getters e Setters
}
