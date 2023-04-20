package com.cocktailmasters.backend.authentication.domain.oauth;

public interface OAuthInfoResponse {

    String getEmail();

    String getNickname();

    String getGender();

    String getAgeRange();

    OAuthProvider getOAuthProvider();
}
