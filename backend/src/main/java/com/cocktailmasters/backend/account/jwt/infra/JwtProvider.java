package com.cocktailmasters.backend.account.jwt.infra;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@PropertySource("security.yml")
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "email";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpirationDate;

    @Value("${jwt.refresh-token.expiation}")
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

    public Optional<String> resolveAccessToken(HttpServletRequest request) {
        return resolveToken(request, accessTokenHeader);
    }

    public Optional<String> resolveRefreshToken(HttpServletRequest request) {
        return resolveToken(request, refreshTokenHeader);
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

    private Optional<String> resolveToken(HttpServletRequest request, String tokenHeader) {
        return Optional.ofNullable(request.getHeader(tokenHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> refreshTokenHeader.replace(BEARER, ""));
    }

    private Date createExpireDate(Long tokenExpirationDate) {
        Date now = new Date();
        return new Date(now.getTime() + tokenExpirationDate);
    }
}
