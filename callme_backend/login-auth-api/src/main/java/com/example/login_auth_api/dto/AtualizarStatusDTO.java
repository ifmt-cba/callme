package com.example.login_auth_api.dto;


import com.example.login_auth_api.domain.user.ChamadoExterno;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class AtualizarStatusDTO {
    private Long chamadoId;
    private ChamadoExterno.StatusChamado novoStatus;

}
