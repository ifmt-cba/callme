// Em: src/main/java/com/example/login_auth_api/services/DashboardService.java
package com.example.login_auth_api.service;

import com.example.login_auth_api.dto.ChamadoStatusDTO;
import com.example.login_auth_api.repositories.ChamadoExternoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ChamadoExternoRepository chamadoexternoRepository;

    public ChamadoStatusDTO getChamadoStatusStats() {
        List<Object[]> results = chamadoexternoRepository.countChamadosByStatus();

        Map<String, Long> statusCounts = results.stream()
                .filter(result -> Objects.nonNull(result[0]))
                .collect(
                        Collectors.groupingBy(
                                result -> result[0].toString().toUpperCase(),
                                Collectors.summingLong(result -> (long) result[1])
                        )
                );

        long abertos = statusCounts.getOrDefault("ABERTO", 0L);
        long emAndamento = statusCounts.getOrDefault("EM_ANDAMENTO", 0L);
        long fechados = statusCounts.getOrDefault("FECHADO", 0L);

        return new ChamadoStatusDTO(abertos, emAndamento, fechados);
    }
}