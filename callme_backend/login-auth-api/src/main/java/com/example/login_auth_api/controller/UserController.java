package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.Role;
import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.CreateUserDto;
import com.example.login_auth_api.dto.UserResponseDTO;
import com.example.login_auth_api.repositories.RoleRepository;
import com.example.login_auth_api.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LogPort log;

    public UserController(BCryptPasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          LogPort log) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.log = log;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {
        log.info(String.format("Criando usuário RT | Username: %s", dto.username()));

        var rtRole = roleRepository.findByName(Role.Values.RT.name());
        var userFromDb = userRepository.findByUsername(dto.username());
        if (userFromDb.isPresent()) {
            log.warn(String.format("Usuário já existe | Username: %s", dto.username()));
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmail(dto.email());
        user.setRoles(Set.of(rtRole));
        userRepository.save(user);

        log.info(String.format("Usuário RT criado com sucesso | Username: %s", dto.username()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("Admin listando usuários");

        var users = userRepository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(users);
    }

    @Transactional
    @PostMapping("/users/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> newUserAdmin(@RequestBody CreateUserDto dto) {
        log.info(String.format("Criando usuário ADMIN | Username: %s", dto.username()));

        var admRole = roleRepository.findByName(Role.Values.ADMIN.name());
        var userFromDb = userRepository.findByUsername(dto.username());
        if (userFromDb.isPresent()) {
            log.warn(String.format("Usuário já existe | Username: %s", dto.username()));
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmail(dto.email());
        user.setRoles(Set.of(admRole));
        userRepository.save(user);

        log.info(String.format("Usuário ADMIN criado com sucesso | Username: %s", dto.username()));
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info(String.format("Admin excluindo do usuário | ID: %s", id));

        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(String.format("Usuário não encontrado | ID: %s", id));
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
                });

        userRepository.delete(user);

        log.info(String.format("Usuário excluído com sucesso | Username: %s | ID: %s", user.getUsername(), id));
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping("/users/update/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody CreateUserDto dto) {
        log.info(String.format("ADM atualizando usuário | ID: %s", id));

        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(String.format("Usuário não encontrado | ID: %s", id));
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
                });

        user.setUsername(dto.username());
        user.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        userRepository.save(user);

        log.info(String.format("Usuário atualizado com sucesso | Username: %s | ID: %s", user.getUsername(), id));
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }
}