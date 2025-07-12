// src/main/java/com/example/login_auth_api/dto/ChamadoUpdateRequestDTO.java
package com.example.login_auth_api.dto;

import com.example.login_auth_api.domain.user.ChamadoExterno;

public record ChamadoUpdateRequestDTO(
        ChamadoExterno.StatusChamado status, // Confirme que é 'status'
        TecnicoDTO tecnico                   // Confirme que é 'tecnico'
        // ... outros campos que você permite atualizar
) {}