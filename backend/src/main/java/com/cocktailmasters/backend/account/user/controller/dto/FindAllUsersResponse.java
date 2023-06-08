package com.cocktailmasters.backend.account.user.controller.dto;

import com.cocktailmasters.backend.account.user.controller.dto.item.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindAllUsersResponse {

    private List<UserDto> users;
}
