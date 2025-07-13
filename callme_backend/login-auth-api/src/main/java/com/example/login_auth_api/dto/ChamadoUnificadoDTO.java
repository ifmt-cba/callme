package com.example.login_auth_api.dto;

import java.time.Instant;

public record ChamadoUnificadoDTO(

        String id,
        String tipo,
        String assunto,
        String descricao,
        String solicitante,
        String status,
        Instant data
){}
