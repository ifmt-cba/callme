package com.example.login_auth_api.controller;

import com.example.login_auth_api.domain.user.Chamado;
import com.example.login_auth_api.domain.user.Role;
import com.example.login_auth_api.dto.CreateChamadoDto;
import com.example.login_auth_api.repositories.ChamadoRepository;
import com.example.login_auth_api.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class ChamadoController {

    private final ChamadoRepository chamadoRepository;

    private final UserRepository userRepository;

    public ChamadoController(ChamadoRepository chamadoRepository, UserRepository userRepository) {

        this.chamadoRepository = chamadoRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/chamados")
    public ResponseEntity<Void>Createchamado(@RequestBody CreateChamadoDto Dto,
                                             JwtAuthenticationToken token) {

            var user = userRepository.findById(UUID.fromString(token.getName()));

           var chamado = new Chamado();
           chamado.setUser(user.get());
           chamado.setContent(Dto.content());

           chamadoRepository.save(chamado);
           return ResponseEntity.ok().build();
    }
    @DeleteMapping("/chamados/{id}")
    public ResponseEntity<Void> deletechamado(@PathVariable ("id") Long chamadoId,
        JwtAuthenticationToken token){
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var chamado = chamadoRepository.findById(chamadoId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (isAdmin || chamado.getUser().getUserid().equals(UUID.fromString(token.getName()))) {
            chamadoRepository.deleteById(chamadoId);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        return ResponseEntity.ok().build();

    }

}
