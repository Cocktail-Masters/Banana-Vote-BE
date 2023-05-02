package com.cocktailmasters.backend.vote.controller.dto.vote;

import com.cocktailmasters.backend.vote.controller.dto.item.SimpleVoteDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FindPopularVotesResponse {

    private List<SimpleVoteDto> votes;
}
