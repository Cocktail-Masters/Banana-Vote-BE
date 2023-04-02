package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PredictionDto {

    private Long voteItemId;
    private Long point;
}
