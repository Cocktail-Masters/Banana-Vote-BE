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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        log.info("Success OAuth2 Login");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            User user = findUserByEmail(oAuth2User.getEmail());
            if (oAuth2User.getRole() == Role.GUEST) {
                String accessToken = jwtService.createAccessToken(user);
                log.info("GUEST access token : " + accessToken);

                response.addHeader(jwtService.getAccessTokenHeader(), "Bearer " + accessToken);
                // response.sendRedirect("/"); // 프론트의 회원가입 추가 정보 입력 폼 리다이렉트
                response.setStatus(HttpServletResponse.SC_OK);
                jwtService.setAccessTokenHeader(response, accessToken);
            } else {
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        log.info("로그인 성공 및 토큰 생성");
        User user = findUserByEmail(oAuth2User.getEmail());
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);

        log.info("USER access token : " + accessToken);

        response.addHeader(jwtService.getAccessTokenHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshTokenHeader(), "Bearer " + refreshToken);

        response.setStatus(HttpServletResponse.SC_OK);
        jwtService.setAccessTokenHeader(response, accessToken);
        jwtService.setRefreshTokenHeader(response, refreshToken);
        user.updateRefreshToken(refreshToken);
        userRepository.saveAndFlush(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(AuthException::new);
    }
}
