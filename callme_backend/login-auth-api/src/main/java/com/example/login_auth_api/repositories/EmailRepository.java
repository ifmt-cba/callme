package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByMessageId(String messageId);
}

