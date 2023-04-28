package com.cocktailmasters.backend.account.jwt.infra;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@PropertySource("security.yml")
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

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

    private String createToken(String tokenType, User user, Long tokenExpirationDate) {
        return Jwts.builder()
                .setSubject(tokenType)
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setExpiration(createExpireDate(tokenExpirationDate))
                .signWith(SignatureAlgorithm.ES256, secretKey)
                .compact();
    }

    private Date createExpireDate(Long tokenExpirationDate) {
        Date now = new Date();
        return new Date(now.getTime() + tokenExpirationDate);
    }
}
