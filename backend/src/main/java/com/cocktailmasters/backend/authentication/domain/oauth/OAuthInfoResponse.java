package com.cocktailmasters.backend.authentication.domain.oauth;

import com.cocktailmasters.backend.account.domain.entity.OAuthProvider;

public interface OAuthInfoResponse {

    String getEmail();

    String getNickname();

    String getGender();

    String getAgeRange();

    OAuthProvider getOAuthProvider();
}
