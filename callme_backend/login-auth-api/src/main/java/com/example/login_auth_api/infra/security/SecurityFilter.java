/*

package com.example.login_auth_api.infra.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import com.example.login_auth_api.domain.user.User;


import java.io.IOException;
import java.util.Collections;

    @Component
    public class SecurityFilter extends OncePerRequestFilter {


        private final TokenService tokenService;
        private final UserRepository userRepository;

        @Autowired
        public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
            this.tokenService = tokenService;
            this.userRepository = userRepository;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            var token = this.recoverToken(request);
            var login = tokenService.validateToken(token);

            if(login != null){
                User user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User Not Found"));
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }

        private String recoverToken(HttpServletRequest request){
            var authHeader = request.getHeader("Authorization");
            if(authHeader == null) return null;
            return authHeader.replace("Bearer ", "");
        }
    }

*/
