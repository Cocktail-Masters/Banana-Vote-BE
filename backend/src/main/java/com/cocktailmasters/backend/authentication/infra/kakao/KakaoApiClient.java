package com.cocktailmasters.backend.authentication.infra.kakao;

import com.cocktailmasters.backend.authentication.domain.oauth.OAuthApiClient;
import com.cocktailmasters.backend.authentication.domain.oauth.OAuthInfoResponse;
import com.cocktailmasters.backend.authentication.domain.oauth.OAuthLoginParams;
import com.cocktailmasters.backend.authentication.domain.oauth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KakaoApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Override
    public OAuthProvider oAuthProvider() {
        return null;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        return null;
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        return null;
    }
}
