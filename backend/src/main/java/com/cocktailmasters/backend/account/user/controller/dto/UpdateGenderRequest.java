package com.cocktailmasters.backend.account.user.controller.dto;

import com.cocktailmasters.backend.account.user.domain.entity.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateGenderRequest {

    @NotNull
    private Gender gender;
}
