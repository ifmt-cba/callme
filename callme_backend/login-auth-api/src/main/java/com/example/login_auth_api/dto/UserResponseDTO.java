package com.example.login_auth_api.dto;

import com.example.login_auth_api.domain.user.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        Set<String> roles
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getUserid(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getName()) // pega o nome da Role (ex: "ADMIN")
                        .collect(Collectors.toSet())
        );
    }
}
