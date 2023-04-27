package com.cocktailmasters.backend.authentication.infra.kakao;

import com.cocktailmasters.backend.authentication.domain.oauth.OAuthInfoResponse;
import com.cocktailmasters.backend.account.domain.entity.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {

        private KakaoProfile profile;
        private String email;
        private String ageRange;
        private String gender;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {

        private String nickname;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getGender() {
        return kakaoAccount.gender;
    }

    @Override
    public String getAgeRange() {
        return kakaoAccount.ageRange;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
