package com.example.login_auth_api.dto;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;


@Data
public class EmailLeituraCompletaDTO {
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

    public EmailLeituraCompletaDTO(String remetente, String assunto, String corpoSimples, String comprovante) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.corpoSimples = corpoSimples;
        this.dataHora = dataHora = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        this.spf = spf;
        this.dkim = dkim;
        this.dmarc = dmarc;
        this.comprovante = comprovante;

    }
}
