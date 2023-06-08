package com.cocktailmasters.backend.vote.controller.dto.vote;

import com.cocktailmasters.backend.vote.controller.dto.item.VoteDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindVotesResponse {

    private Long totalCount;
    private List<VoteDto> votes;
}
