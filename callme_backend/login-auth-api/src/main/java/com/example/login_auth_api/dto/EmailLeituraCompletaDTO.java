package com.example.login_auth_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailLeituraCompletaDTO {
    private String remetente;
    private String assunto;
    private String corpoSimples;
    private String comprovante;

    public EmailLeituraCompletaDTO() {
        // Construtor padrão necessário para Jackson
    }

    public EmailLeituraCompletaDTO(String remetente, String assunto, String corpoSimples, String comprovante) {
        this.remetente = remetente;
        this.assunto = assunto;
        this.corpoSimples = corpoSimples;
        this.comprovante = comprovante;
    }
}
