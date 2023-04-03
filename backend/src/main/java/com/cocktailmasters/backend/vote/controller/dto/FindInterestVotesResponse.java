package com.cocktailmasters.backend.vote.controller.dto;

import com.cocktailmasters.backend.vote.controller.dto.item.SimpleVoteDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindInterestVotesResponse {

    private List<SimpleVoteDto> votes;
}
