package com.cocktailmasters.backend.account.controller.dto;

import com.cocktailmasters.backend.account.domain.entity.Gender;
import com.cocktailmasters.backend.account.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpRequest {

    private String email;
    private String password;
    private String nickname;
    private Gender gender;

    public User toUserEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .gender(gender)
                .build();
    }
}
