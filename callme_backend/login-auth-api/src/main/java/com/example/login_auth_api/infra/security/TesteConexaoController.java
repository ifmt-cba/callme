package com.example.login_auth_api.infra.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class TesteConexaoController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/testar-conexao")
    public String testarConexao() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return "✅ Conectado com sucesso ao banco de dados!";
            } else {
                return "⚠️ Conexão inválida.";
            }
        } catch (Exception e) {
            return "❌ Erro ao conectar com o banco: " + e.getMessage();
        }
    }
}
