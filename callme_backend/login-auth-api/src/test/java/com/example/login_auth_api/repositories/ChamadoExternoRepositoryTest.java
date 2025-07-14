package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.user.ChamadoExterno;
import com.example.login_auth_api.domain.user.ChamadoExterno.StatusChamado;
import com.example.login_auth_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ChamadoExternoRepositoryTest {

    @Autowired
    private ChamadoExternoRepository chamadoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve salvar e buscar chamado por tokenEmail")
    void testFindByTokenEmail() {
        ChamadoExterno chamado = new ChamadoExterno();
        chamado.setRemetente("julio@email.com");
        chamado.setAssunto("Erro no sistema");
        chamado.setDescricao("Descrição teste");
        chamado.setTokenEmail("token-123");
        chamado.setStatus(StatusChamado.ABERTO);

        chamadoRepository.save(chamado);

        Optional<ChamadoExterno> resultado = chamadoRepository.findByTokenEmail("token-123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getAssunto()).isEqualTo("Erro no sistema");
    }

    @Test
    @DisplayName("Deve contar chamados por status")
    void testCountChamadosByStatus() {
        ChamadoExterno c1 = new ChamadoExterno();
        c1.setTokenEmail("token-a");
        c1.setStatus(StatusChamado.ABERTO);
        chamadoRepository.save(c1);

        ChamadoExterno c2 = new ChamadoExterno();
        c2.setTokenEmail("token-b");
        c2.setStatus(StatusChamado.FECHADO);
        chamadoRepository.save(c2);

        ChamadoExterno c3 = new ChamadoExterno();
        c3.setTokenEmail("token-c");
        c3.setStatus(StatusChamado.ABERTO);
        chamadoRepository.save(c3);

        List<Object[]> resultado = chamadoRepository.countChamadosByStatus();

        assertThat(resultado).hasSizeGreaterThanOrEqualTo(2); // ajuste seguro
        for (Object[] linha : resultado) {
            StatusChamado status = (StatusChamado) linha[0];
            Long count = (Long) linha[1];

            if (status == StatusChamado.ABERTO) {
                assertThat(count).isEqualTo(2);
            }
        }
    }

    @Test
    @DisplayName("Deve encontrar chamados por técnico")
    void testFindByTecnico() {
        User tecnico = new User();
        tecnico.setUsername("julio");
        tecnico.setEmail("julio@rt.com");
        tecnico.setToken(UUID.randomUUID().toString());
        tecnico = userRepository.save(tecnico); // garantir entidade gerenciada

        ChamadoExterno c1 = new ChamadoExterno();
        c1.setTokenEmail("t1");
        c1.setTecnico(tecnico);
        chamadoRepository.save(c1);

        List<ChamadoExterno> resultado = chamadoRepository.findByTecnico(tecnico);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTecnico().getUsername()).isEqualTo("julio");
    }
}
