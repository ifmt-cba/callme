package com.example.login_auth_api.dto;

import java.time.Instant;

public record ChamadoUnificadoDTO(
     Long id,
    String tipo,
    String assunto,
    String descricao,
    String solicitante,
    Instant data
){}
