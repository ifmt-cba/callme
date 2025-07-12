// Em um novo arquivo: src/main/java/com/example/login_auth_api/controllers/DashboardController.java
package com.example.login_auth_api.controller;

import com.example.login_auth_api.dto.ChamadoStatusDTO;
import com.example.login_auth_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/chamado-stats")
    public ResponseEntity<ChamadoStatusDTO> getChamadoStats() {
        return ResponseEntity.ok(dashboardService.getChamadoStatusStats());
    }
}