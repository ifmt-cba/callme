package com.example.login_auth_api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
@Disabled
@SpringBootTest
class LoginAuthApiApplicationTests {

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4-alpine")
        .withDatabaseName("test_db")
        .withUsername("admin")
        .withPassword("admin");

    static {
        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }
		
	@Test
	void contextLoads() {
	}

}
