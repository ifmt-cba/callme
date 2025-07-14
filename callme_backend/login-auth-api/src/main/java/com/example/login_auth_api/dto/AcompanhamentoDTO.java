package com.example.login_auth_api.dto;

import java.util.List; // Importe a classe List

public record AcompanhamentoDTO(
        String status,
        String dataAbertura,
        List<ComentarioDTO> comentarios
) {}