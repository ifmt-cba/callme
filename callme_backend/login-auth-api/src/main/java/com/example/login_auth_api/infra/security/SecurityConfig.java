package com.example.login_auth_api.infra.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;




import javax.swing.plaf.nimbus.NimbusStyle;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.security.authorization.AuthenticatedAuthorizationManager.anonymous;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    //Aqui eu fiz a declarei a chave privada e a chave publica

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;


    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authirze -> authirze.
                        requestMatchers("/api/auth/**", "/api/password/**").permitAll().
                        requestMatchers(HttpMethod.POST,"/api/password/reset").permitAll().
                        requestMatchers(HttpMethod.POST,"/api/password/forgot").permitAll().
                        requestMatchers(HttpMethod.GET, "/feed").permitAll().
                        requestMatchers(HttpMethod.GET, "/login").permitAll().
                        requestMatchers(HttpMethod.POST, "/login").permitAll().
                        requestMatchers(HttpMethod.POST, "/email").permitAll().
                        requestMatchers(HttpMethod.GET, "/emails/completo").permitAll().
                        requestMatchers(HttpMethod.GET, "/emails/resumo").permitAll().
                        requestMatchers(HttpMethod.GET, "/anexos/visualizar/{id}").permitAll().
                        requestMatchers(HttpMethod.GET, "/chamados/abrir").permitAll().
                        requestMatchers(HttpMethod.GET, "/token/{tokenEmail}").permitAll().
                        requestMatchers(HttpMethod.GET, "chamados/buscar/{token}").permitAll().
                        requestMatchers(HttpMethod.PUT, "/chamados/{{tokenEmail}}/status").permitAll().
                        requestMatchers(HttpMethod.POST, "/users").permitAll().
                        anyRequest().authenticated())
                .csrf(csrf ->csrf.disable())
                .oauth2ResourceServer(ouath ->ouath.jwt(Customizer.withDefaults()).authenticationEntryPoint((request, response, authException) -> {
                            // se não tiver token, deixa passar se for rota pública
                            if (request.getRequestURI().startsWith("/api/password")) {
                                response.setStatus(HttpServletResponse.SC_OK);
                            } else {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                            }
                        })
                ).anonymous(Customizer.withDefaults())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        return NimbusJwtDecoder.withPublicKey(publicKey).build();


    }

    @Bean
    public JwtEncoder jwtEncoder() {

        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();

        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }
}
