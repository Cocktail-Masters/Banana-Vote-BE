package com.cocktailmasters.backend.account.user.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateAgeRequest {

    @NotNull
    private int age;
}
