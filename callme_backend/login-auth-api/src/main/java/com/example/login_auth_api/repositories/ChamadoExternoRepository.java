package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.ChamadoExterno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChamadoExternoRepository extends JpaRepository<ChamadoExterno, Long> {
    Optional<ChamadoExterno> findByMessageId(String messageId);
    Optional<ChamadoExterno> findByTokenEmail(String tokenEmail);

}
