package com.cocktailmasters.backend.vote.controller.dto.opinion;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FindOpinionNumberResponse {

    private int opinionNumber;
}
