package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.SendEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmailRepository extends JpaRepository<SendEmail, Long> {
    Optional<SendEmail> findByMessageId(String messageId);
}

