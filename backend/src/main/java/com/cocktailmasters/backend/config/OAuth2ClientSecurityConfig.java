package com.cocktailmasters.backend.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class OAuth2ClientSecurityConfig {

    private static final String CONTEXT_PATH = "/api/v1/members";

    public SecurityFilterChain filterChain(HttpSecurity http, OAuth2ResourceServerProperties.Jwt jwt, TokenService tokenService) throws Exception {
        http
                .authorizeRequests(authorize -> authorize.anyRequest()
                        .authenticated())
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
