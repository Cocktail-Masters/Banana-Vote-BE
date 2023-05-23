package com.cocktailmasters.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${jwt.access-token.header}")
    private String accessTokenHeader;

    @Value("${jwt.refresh-token.header}")
    private String refreshTokenHeader;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .exposedHeaders(accessTokenHeader, refreshTokenHeader)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH");
    }
}
