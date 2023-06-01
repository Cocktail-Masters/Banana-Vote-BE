package com.cocktailmasters.backend.account.user.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignInRequest {

    @NotNull
    String email;

    @NotNull
    String password;
}
