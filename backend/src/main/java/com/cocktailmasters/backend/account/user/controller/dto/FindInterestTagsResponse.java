package com.cocktailmasters.backend.account.user.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindInterestTagsResponse {

    private List<String> tags;
}
