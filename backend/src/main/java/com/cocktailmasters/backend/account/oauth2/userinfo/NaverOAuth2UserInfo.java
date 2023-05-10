package com.cocktailmasters.backend.account.oauth2.userinfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response.isEmpty()) {
            return null;
        }
        return (String) response.get("id");
    }

    @Override
    public String getNickname() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response.isEmpty()) {
            return null;
        }
        return (String) response.get("nickname");
    }

    @Override
    public String getGender() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response.isEmpty()) {
            return null;
        }
        return (String) response.get("gender");
    }

    @Override
    public String getAge() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response.isEmpty()) {
            return null;
        }
        return (String) response.get("age");
    }
}
