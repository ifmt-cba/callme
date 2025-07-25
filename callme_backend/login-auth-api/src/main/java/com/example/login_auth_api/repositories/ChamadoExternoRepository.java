package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.ChamadoExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.login_auth_api.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface ChamadoExternoRepository extends JpaRepository<ChamadoExterno, Long> {
    Optional<ChamadoExterno> findByMessageId(String messageId);
    Optional<ChamadoExterno> findByTokenEmail(String tokenEmail);

    @Query("SELECT c.status, COUNT(c) FROM ChamadoExterno c GROUP BY c.status")
    List<Object[]> countChamadosByStatus();

    List<ChamadoExterno> findByTecnico(User tecnico);

    List<ChamadoExterno> findByStatus(ChamadoExterno.StatusChamado status);

    List<ChamadoExterno> findByTecnicoAndStatusIsNot(User tecnico, ChamadoExterno.StatusChamado status);


}


