package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.Role;
import com.example.login_auth_api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Long> {

}