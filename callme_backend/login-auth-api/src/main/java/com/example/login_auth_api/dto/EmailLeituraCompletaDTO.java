package com.example.login_auth_api.dto;

import lombok.Data;

@Data
public class EmailLeituraCompletaDTO {
    private String token;
    private String remetente;
    private String destinatario;
    private String assunto;
    private String corpoSimples;
    private String dataHora;
    private String spf;
    private String dkim;
    private String dmarc;
    private String comprovante;

    public EmailLeituraCompletaDTO() {
        // Construtor padrão necessário para Jackson
    }

    // Construtor com todos os parâmetros que você usa no serviço
    public EmailLeituraCompletaDTO(String token, String remetente, String destinatario,
                                   String assunto, String corpoSimples, String dataHora,
                                   String spf, String dkim, String dmarc, String comprovante) {
        this.token = token;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.corpoSimples = corpoSimples;
        this.dataHora = dataHora;
        this.spf = spf;
        this.dkim = dkim;
        this.dmarc = dmarc;
        this.comprovante = comprovante;
    }
}
