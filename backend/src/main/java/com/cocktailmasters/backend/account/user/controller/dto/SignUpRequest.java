package com.cocktailmasters.backend.account.user.controller.dto;

import com.cocktailmasters.backend.account.user.domain.entity.Gender;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    private String nickname;
    private int age;
    private Gender gender;

    public User toUserEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .age(age)
                .nickname(nickname)
                .gender(gender)
                .build();
    }
}
