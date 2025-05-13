package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.AnexoEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnexoEmailRepository extends JpaRepository<AnexoEmail, Long> {
}
