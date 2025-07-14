package com.example.login_auth_api.dto;

import java.time.Instant;

public record AcompanhamentoDTO(
        String status,
        Instant dataAbertura
) {}