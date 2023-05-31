package com.cocktailmasters.backend.account.user.controller.dto;


import com.cocktailmasters.backend.account.user.domain.entity.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindUserInfoResponse {

    private String nickname;
    private int age;
    private Gender gender;
    private Long ranking;
    private Long percentage;
}
