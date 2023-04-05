package com.cocktailmasters.backend.vote.controller.dto.vote;

import com.cocktailmasters.backend.vote.controller.dto.item.PredictionDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePredictionRequest {

    private PredictionDto prediction;
}
