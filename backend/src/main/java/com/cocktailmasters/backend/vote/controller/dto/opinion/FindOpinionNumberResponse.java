package com.cocktailmasters.backend.vote.controller.dto.opinion;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindOpinionNumberResponse {

    private int opinionNumber;
}
