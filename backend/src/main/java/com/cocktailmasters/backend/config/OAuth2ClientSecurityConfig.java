package com.cocktailmasters.backend.config;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2ClientSecurityConfig {

    private static final String CONTEXT_PATH = "/api/v1/members";

    private final OAuth2ResourceServerConfigurer.JwtConfigurer jwtConfigurer;

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain filterChain(HttpSecurity http, OAuth2ResourceServerProperties.Jwt jwt, TokenService tokenService) throws Exception {
        http
                .authorizeRequests(authorize -> authorize.anyRequest()
                        .authenticated())
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
