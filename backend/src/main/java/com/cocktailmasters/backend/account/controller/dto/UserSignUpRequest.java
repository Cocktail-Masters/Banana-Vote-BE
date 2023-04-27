package com.cocktailmasters.backend.account.controller.dto;

import com.cocktailmasters.backend.account.domain.entity.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpRequest {

    private String email;
    private String password;
    private String nickname;
    private Gender gender;
}
