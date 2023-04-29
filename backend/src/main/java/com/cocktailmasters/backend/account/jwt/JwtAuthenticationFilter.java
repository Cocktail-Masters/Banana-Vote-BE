package com.cocktailmasters.backend.account.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            if (requestURI.startsWith("/api/v1/users/login")) {
                filterChain.doFilter(request, response);
                return;
            }
            String accessToken = jwtProvider.extractAccessToken(request)
                    .filter(jwtProvider::validateToken)
                    .orElse(null);
            String refreshToken = jwtProvider.extractRefreshToken(request)
                    .filter(jwtProvider::validateToken)
                    .orElse(null);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
        }

    }
}
