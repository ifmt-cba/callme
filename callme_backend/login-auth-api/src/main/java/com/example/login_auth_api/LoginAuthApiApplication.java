package com.example.login_auth_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoginAuthApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(LoginAuthApiApplication.class, args);
	}

}
