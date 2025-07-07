package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByToken(String token);
    Optional<User> findByUsername(String username);
    // NOVO MÉTODO: Busca todos os usuários que possuem um Role com o nome especificado.
    // Supondo que seus técnicos tenham o perfil "RT" ou "ADMIN".
    List<User> findByRoles_Name(String roleName);
}