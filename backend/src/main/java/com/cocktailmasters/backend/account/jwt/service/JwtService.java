package com.cocktailmasters.backend.account.jwt.service;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.util.exception.AuthException;
import com.cocktailmasters.backend.util.exception.NotFoundUserException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class JwtService {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpirationDate;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationDate;

    @Value("${jwt.access-token.header}")
    private String accessTokenHeader;

    @Value("${jwt.refresh-token.header}")
    private String refreshTokenHeader;

    private final UserRepository userRepository;

    public String createAccessToken(User user) {
        Claims claims = Jwts.claims();
        claims.put(ID_CLAIM, user.getId());
        claims.put(EMAIL_CLAIM, user.getEmail());
        claims.put(ROLE_CLAIM, user.getRole());
        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setClaims(claims)
                .setExpiration(createExpireDate(accessTokenExpirationDate))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(User user) {
        Claims claims = Jwts.claims();
        claims.put(ID_CLAIM, user.getId());
        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setClaims(claims)
                .setExpiration(createExpireDate(refreshTokenExpirationDate))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessTokenHeader, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshTokenHeader, refreshToken);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return extractToken(request, accessTokenHeader);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return extractToken(request, refreshTokenHeader);
    }

    public Map<String, Object> getPayload(String token) {
        try {
            final Claims body = extractBody(token);
            return body.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException();
        }
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature");
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT token is invalid");
            return false;
        }
    }

    public User findUserByToken(String token) {
        return userRepository.findByEmail((String) getPayload(token.replace(BEARER, "")).get(EMAIL_CLAIM))
                .orElseThrow(() -> new NotFoundUserException());
    }

    private Date createExpireDate(Long tokenExpirationDate) {
        Date now = new Date();
        return new Date(now.getTime() + tokenExpirationDate);
    }

    private Optional<String> extractToken(HttpServletRequest request, String tokenHeader) {
        return Optional.ofNullable(request.getHeader(tokenHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    private Claims extractBody(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthException();
        }
    }
}
