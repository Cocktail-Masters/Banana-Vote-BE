package com.cocktailmasters.backend.account.user.controller.dto.item;

import com.cocktailmasters.backend.account.user.domain.entity.Role;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {

    private long id;
    private String nickname;
    private Role role;
    private boolean isActive;
}
