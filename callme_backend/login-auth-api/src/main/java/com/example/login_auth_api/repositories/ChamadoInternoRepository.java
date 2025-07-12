package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.ChamadoInterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChamadoInternoRepository extends JpaRepository<ChamadoInterno, Long> {

}