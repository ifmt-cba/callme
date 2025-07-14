// Em: src/main/java/com/example/login_auth_api/dto/ComentarioDTO.java
package com.example.login_auth_api.dto;

public record ComentarioDTO(
        String texto,
        String dataCriacao,
        String autorUsername
) {}