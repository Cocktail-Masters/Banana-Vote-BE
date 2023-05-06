package com.cocktailmasters.backend.account.jwt.service;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.util.exception.AuthException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Getter
@PropertySource("application-dev.yml")
@RequiredArgsConstructor
@Component
public class JwtProvider {

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

    public String createAccessToken(User user) {
        return createToken(ACCESS_TOKEN_SUBJECT, user, accessTokenExpirationDate);
    }

    public String createRefreshToken(User user) {
        return createToken(REFRESH_TOKEN_SUBJECT, user, accessTokenExpirationDate);
    }

    public void setAccessTokenHeader(HttpServletResponse response, String token) {
        response.setHeader(accessTokenHeader, token);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String token) {
        response.setHeader(refreshTokenHeader, token);
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

    private String createToken(String tokenType, User user, Long tokenExpirationDate) {
        Claims claims = Jwts.claims();
        claims.put(ID_CLAIM, user.getId());
        claims.put(EMAIL_CLAIM, user.getEmail());
        claims.put(ROLE_CLAIM, user.getRole());
        return Jwts.builder()
                .setSubject(tokenType)
                .setClaims(claims)
                .setExpiration(createExpireDate(tokenExpirationDate))
                .signWith(SignatureAlgorithm.ES256, secretKey)
                .compact();
    }

    private Date createExpireDate(Long tokenExpirationDate) {
        Date now = new Date();
        return new Date(now.getTime() + tokenExpirationDate);
    }

    private Optional<String> extractToken(HttpServletRequest request, String tokenHeader) {
        return Optional.ofNullable(request.getHeader(tokenHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> refreshTokenHeader.replace(BEARER, ""));
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
