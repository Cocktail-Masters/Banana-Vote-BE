package com.cocktailmasters.backend.account.jwt;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            if (requestURI.startsWith("/api/v1/users/login")) {
                filterChain.doFilter(request, response);
                return;
            }
            String refreshToken = jwtProvider.extractRefreshToken(request)
                    .filter(jwtProvider::validateToken)
                    .orElse(null);
            if (refreshToken != null) {
                checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            }
            if (refreshToken == null) {
                // refresh null 이면 access 검증
                // access가 없거나 유효하지 않으면 403
            }

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString("Access Token is expired"));
            response.getWriter().flush();
        }
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
                                                       String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    jwtProvider.setAccessTokenHeader(response, jwtProvider.createAccessToken(user));
                });
    }

    private String reIssueRefreshToken(User user) {
        String reIssueRefreshToken = jwtProvider.createRefreshToken(user);
        user.updateRefreshToken(reIssueRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssueRefreshToken;
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   FilterChain filterChain) throws ServletException, IOException {
        jwtProvider.extractAccessToken(request)
                .filter(jwtProvider::validateToken)
                .ifPresent(accessToken -> jwtProvider.extractEmail(accessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));
        filterChain.doFilter(request, response);
    }
}
