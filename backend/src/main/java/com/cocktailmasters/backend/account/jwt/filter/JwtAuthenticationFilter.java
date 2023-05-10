package com.cocktailmasters.backend.account.jwt.filter;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.jwt.util.PasswordUtil;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String EMAIL_CLAIM = "email";
    private static final String ID_CLAIM = "id";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

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
            String refreshToken = jwtService.extractRefreshToken(request)
                    .filter(jwtService::validateToken)
                    .orElse(null);
            if (refreshToken != null) {
                checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
                return;
            }
            if (refreshToken == null) {
                checkAccessTokenAndAuthentication(request, response, filterChain);
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
                    reIssueRefreshToken(user);
                    response.setStatus(HttpServletResponse.SC_OK);
                    jwtService.setAccessTokenHeader(response, jwtService.createAccessToken(user));
                    jwtService.setRefreshTokenHeader(response, jwtService.createRefreshToken(user));
                });
    }

    private String reIssueRefreshToken(User user) {
        String reIssueRefreshToken = jwtService.createRefreshToken(user);
        user.updateRefreshToken(reIssueRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssueRefreshToken;
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request)
                .filter(jwtService::validateToken)
                .ifPresent(accessToken -> {
                    Long userId = (Long) jwtService.getPayload(accessToken).get(ID_CLAIM);
                    userRepository.findById(userId)
                            .ifPresent(this::saveAuthentication);
                });
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(User user) {
        String password = user.getPassword();
        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
        }
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(password)
                .roles(user.getRole().name())
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser,
                null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
