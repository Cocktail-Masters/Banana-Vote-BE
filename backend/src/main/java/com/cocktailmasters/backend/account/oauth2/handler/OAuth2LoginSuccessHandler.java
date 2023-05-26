package com.cocktailmasters.backend.account.oauth2.handler;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.oauth2.domain.CustomOAuth2User;
import com.cocktailmasters.backend.account.user.domain.entity.Role;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.util.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${front.redirect-uri}")
    private String frontRedirectUri;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("Success OAuth2 Login");
        String redirectUrl;
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            User user = findUserByEmail(oAuth2User.getEmail());
            if (oAuth2User.getRole() == Role.GUEST) {
                String accessToken = jwtService.createAccessToken(user);

                log.info("GUEST access token : " + accessToken);

                jwtService.setAccessTokenHeader(response, accessToken);
                redirectUrl = makeRedirectUrlWithAccess(accessToken);
            } else {
                redirectUrl = loginSuccess(response, oAuth2User);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } catch (Exception e) {
            throw e;
        }
    }

    private String makeRedirectUrlWithAccess(String accessToken) {
        return UriComponentsBuilder.fromUriString(frontRedirectUri + accessToken)
                .build()
                .toUriString();
    }

    private String makeRedirectUrlWithAccessAndRefresh(String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString(frontRedirectUri + accessToken + "_" + refreshToken)
                .build()
                .toUriString();
    }

    private String loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        log.info("로그인 성공 및 토큰 생성");
        User user = findUserByEmail(oAuth2User.getEmail());
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);

        log.info("access token : " + accessToken);
        log.info("refresh token : " + accessToken);

        jwtService.setAccessTokenHeader(response, accessToken);
        jwtService.setRefreshTokenHeader(response, refreshToken);
        user.updateRefreshToken(refreshToken);
        userRepository.saveAndFlush(user);

        return makeRedirectUrlWithAccessAndRefresh(accessToken, refreshToken);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(AuthException::new);
    }
}
