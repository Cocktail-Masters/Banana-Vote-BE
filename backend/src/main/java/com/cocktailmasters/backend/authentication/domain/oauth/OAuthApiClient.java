package com.cocktailmasters.backend.authentication.domain.oauth;

import com.cocktailmasters.backend.account.domain.entity.OAuthProvider;

public interface OAuthApiClient {

    OAuthProvider oAuthProvider();

    String requestAccessToken(OAuthLoginParams params);

    OAuthInfoResponse requestOauthInfo(String accessToken);
}
