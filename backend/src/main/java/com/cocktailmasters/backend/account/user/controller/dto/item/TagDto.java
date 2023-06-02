package com.cocktailmasters.backend.account.user.controller.dto.item;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TagDto {

    @NotNull
    private String tagName;
}
