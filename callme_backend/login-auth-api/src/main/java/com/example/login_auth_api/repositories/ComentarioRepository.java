// Em: src/main/java/com/example/login_auth_api/repositories/ComentarioRepository.java
package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}