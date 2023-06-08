package com.cocktailmasters.backend.account.user.controller.dto;


import com.cocktailmasters.backend.account.user.domain.entity.Gender;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindUserInfoResponse {

    private String nickname;
    private String equippedBadgeImageUrl;
    private int age;
    private Gender gender;
    private Long ranking;
    private Long points;
}
