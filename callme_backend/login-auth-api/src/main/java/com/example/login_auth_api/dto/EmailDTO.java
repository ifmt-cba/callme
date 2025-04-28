package com.example.login_auth_api.dto;
//TESTE PARA RECEBER EMAIL
public class EmailDTO {
    private String assunto;
    private String remetente;
    private String conteudo;

    public EmailDTO(String assunto, String remetente, String conteudo) {
        this.assunto = assunto;
        this.remetente = remetente;
        this.conteudo = conteudo;
    }

    // Getters e Setters
    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}