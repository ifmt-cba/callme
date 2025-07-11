package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.ports.LogPort;
import com.example.login_auth_api.domain.user.Role;
import com.example.login_auth_api.dto.LoginRequestDTO;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LogPort log;

    public TokenController(JwtEncoder jwtEncoder,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           LogPort log) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.log = log;
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.username();
        log.info(String.format("Login iniciado | Username: %s", username));

        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty() || !userOpt.get().isLoginCorrect(loginRequestDTO, passwordEncoder)) {
            log.warn(String.format("Falha de login | Username: %s"));
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }

        var user = userOpt.get();
        var nowInstant = Instant.now();
        var expiresIn = 800L;

        var scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getUserid().toString())
                .issuedAt(nowInstant)
                .expiresAt(nowInstant.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        log.info(String.format("Login bem-sucedido | UserID: %s | Escopos: [%s]", user.getUserid(), scopes));

        return ResponseEntity.ok(new ResponseDTO(jwtValue, expiresIn));
    }
}
