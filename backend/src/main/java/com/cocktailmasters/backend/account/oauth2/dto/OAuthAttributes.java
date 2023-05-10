package com.cocktailmasters.backend.account.oauth2.dto;

import com.cocktailmasters.backend.account.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.cocktailmasters.backend.account.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.cocktailmasters.backend.account.oauth2.userinfo.NaverOAuth2UserInfo;
import com.cocktailmasters.backend.account.oauth2.userinfo.OAuth2UserInfo;
import com.cocktailmasters.backend.account.user.domain.entity.Gender;
import com.cocktailmasters.backend.account.user.domain.entity.Role;
import com.cocktailmasters.backend.account.user.domain.entity.SocialType;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.util.exception.AuthException;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if (socialType == SocialType.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        } else if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        } else if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        } else {
            throw new AuthException("Unsupported social type");
        }
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public User toUserEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .email(UUID.randomUUID() + "@social.com")
                .nickname(oAuth2UserInfo.getNickname())
                .gender(Gender.findByGenderName(oAuth2UserInfo.getGender()))
                .role(Role.GUEST)
                .build();
    }
}
