package com.cocktailmasters.backend.vote.controller.dto.opinion;

import com.cocktailmasters.backend.vote.controller.dto.item.OpinionDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindOpinionsResponse {

    List<OpinionDto> opinions;
    List<Long> bestIds;
    int opinionNumber;
}
