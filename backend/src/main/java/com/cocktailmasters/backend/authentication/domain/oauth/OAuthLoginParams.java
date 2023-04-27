package com.cocktailmasters.backend.authentication.domain.oauth;

import com.cocktailmasters.backend.account.domain.entity.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {

    OAuthProvider oAuthProvider();

    MultiValueMap<String, String> makeBody();
}