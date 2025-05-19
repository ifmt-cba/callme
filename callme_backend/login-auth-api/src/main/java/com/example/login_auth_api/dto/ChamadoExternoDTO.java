package com.example.login_auth_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class ChamadoExternoDTO {
    private Long chamadoID;
    private String content;
    private String emailToken;
    private LocalDateTime creationTimestamp;

}
