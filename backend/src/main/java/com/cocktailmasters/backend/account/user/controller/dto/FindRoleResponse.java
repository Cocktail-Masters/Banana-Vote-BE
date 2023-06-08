package com.cocktailmasters.backend.account.user.controller.dto;

import com.cocktailmasters.backend.account.user.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindRoleResponse {

    private Role role;
}
