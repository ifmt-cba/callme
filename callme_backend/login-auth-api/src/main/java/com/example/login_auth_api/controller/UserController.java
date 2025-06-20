package com.example.login_auth_api.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


import java.util.Set;
import java.util.UUID;

@RestController
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;

        this.userRepository = userRepository;

        this.roleRepository = roleRepository;
    }
    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto){


        var RtRole = roleRepository.findByName(Role.Values.RT.name());

        var userFromDb = userRepository.findByUsername(dto.username());
        if (userFromDb.isPresent()) {

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        }

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmail(dto.email());
        user.setRoles(Set.of(RtRole));
        userRepository.save(user);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        var users = userRepository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(users);
    }
    @Transactional
    @PostMapping("/users/admin")//Criacao de admin pelo Proprio ADMIN
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> newUserAdmin(@RequestBody CreateUserDto dto){
        var ADMRole = roleRepository.findByName(Role.Values.ADMIN.name());
        var userFromDb = userRepository.findByUsername(dto.username());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmail(dto.email());
        user.setRoles(Set.of(ADMRole));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
    @Transactional
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<Void>deleteuser(@PathVariable UUID id ){
         var user = userRepository.findById(id).
                 orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario nao encontrado"));
         userRepository.delete(user);
         return ResponseEntity.noContent().build();
        }

    @Transactional
    @PutMapping("/users/update/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody CreateUserDto dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // Atualiza os campos do usuário
        user.setUsername(dto.username());
        user.setEmail(dto.email());

        // Só atualiza a senha se uma nova senha foi fornecida
        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        // Salva as alterações
        userRepository.save(user);

        // Retorna o usuário atualizado
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }
}

