
package com.example.login_auth_api.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;

    private final UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {

        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    @PostMapping("/login")

    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

       var user = userRepository.findByUsername(loginRequestDTO.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequestDTO, passwordEncoder) ){

            throw new BadCredentialsException("Usuario ou senha invalidos leticia teste !");

        }

        var now = Instant.now();

        var expiresIn = 800L;

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));


        var clains = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getUserid().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(clains)).getTokenValue();

        return ResponseEntity.ok(new ResponseDTO(jwtValue,expiresIn));
    }

}
